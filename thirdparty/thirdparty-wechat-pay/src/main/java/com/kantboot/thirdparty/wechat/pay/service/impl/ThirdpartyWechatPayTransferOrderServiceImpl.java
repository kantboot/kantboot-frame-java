package com.kantboot.thirdparty.wechat.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.functional.email.domain.entity.FunctionalEmail;
import com.kantboot.functional.email.dto.EmailMessageDTO;
import com.kantboot.functional.email.service.IFunctionalEmailService;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.functional.pay.service.IFunctionalPayTransferOrderService;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.pay.dao.repository.ThirdpartyWechatPayTransferOrderRepository;
import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayTransferOrder;
import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatPayTransferOrderService;
import com.kantboot.thirdparty.wechat.pay.setting.ThirdpartyWechatPaySetting;
import com.kantboot.thirdparty.wechat.pay.util.WechatTransferCancel;
import com.kantboot.thirdparty.wechat.pay.util.WechatTransferCreate;
import com.kantboot.thirdparty.wechat.pay.util.WechatTransferQuery;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ThirdpartyWechatPayTransferOrderServiceImpl
        implements IThirdpartyWechatPayTransferOrderService {

    @Resource
    private IFunctionalPayTransferOrderService transferOrderService;

    @Resource
    private ThirdpartyWechatPayTransferOrderRepository repository;

    @Resource
    private IThirdpartyWechatMiniprogramService thirdpartyWechatMiniprogramService;

    @Resource
    private ThirdpartyWechatMiniprogramSetting thirdpartyWechatMiniprogramSetting;

    @Resource
    private ThirdpartyWechatPaySetting wechatPaySetting;

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private IFunctionalEmailService emailService;

    private String lockKeyOfCreate(Long orderId) {
        return "wx:transfer:create:" + orderId;
    }

    private String lockKeyOfCancel(Long orderId) {
        return "wx:transfer:cancel:" + orderId;
    }

    private ThirdpartyWechatPayTransferOrder mustGetLatestByTransferOrderId(Long orderId) {
        ThirdpartyWechatPayTransferOrder latest = repository.findTopByTransferOrderIdOrderByIdDesc(orderId);
        if (latest == null) {
            throw BaseException.of("transferOrderNotFound", "转账订单不存在", "zh_CN");
        }
        return latest;
    }

    @Override
    public Object getTransferResultMiniprogram(Long orderId, String code) {
        // ✅ 分布式锁：防止同一个 orderId 并发创建多条
        String lockKey = lockKeyOfCreate(orderId);
        Boolean locked = cacheUtil.lock(lockKey, 15, TimeUnit.SECONDS);
        if (locked == null || !locked) {
            // 没拿到锁：直接返回库里“最新那条”（幂等）
            ThirdpartyWechatPayTransferOrder latest = repository.findTopByOutBillNoOrderByIdDesc(orderId + "");
            if (latest != null) return latest;
            // 如果库里也没有（极端并发第一波），就友好提示重试
            throw BaseException.of("transferCreating", "正在创建转账，请稍后重试", "zh_CN");
        }

        try {
            // ✅ 幂等：如果已经创建过（packageInfo 有值），直接返回，不再调微信 create
            ThirdpartyWechatPayTransferOrder existed = repository.findTopByOutBillNoOrderByIdDesc(orderId + "");
            if (existed != null && StrUtil.isNotEmpty(existed.getPackageInfo())
                    && StrUtil.isEmpty(existed.getErrorCode())) {
                return existed;
            }

            FunctionalPayTransferOrder byId = transferOrderService.getById(orderId);

            WechatTransferCreate wechatTransferCreate = new WechatTransferCreate();
            wechatTransferCreate.setThirdpartyWechatMiniprogramService(thirdpartyWechatMiniprogramService);
            wechatTransferCreate.setThirdpartyWechatMiniprogramSetting(thirdpartyWechatMiniprogramSetting);
            wechatTransferCreate.setWechatPaySetting(wechatPaySetting);

            if (StrUtil.isEmpty(wechatPaySetting.getPayNotifyURL())) {
                wechatTransferCreate.setNotifyUrl("https://kantboot.com/api/wechat/pay/transfer/notify");
            } else {
                wechatTransferCreate.setNotifyUrl(wechatPaySetting.getPayNotifyURL());
            }

            wechatTransferCreate.setTransferAmount(byId.getAmount());
            wechatTransferCreate.setTransferRemark(byId.getDescription());
            wechatTransferCreate.setOutBillNo(orderId + "");

            WechatTransferCreate.TransferCreateResult transferBill =
                    wechatTransferCreate.createTransferBill(orderId + "", code, byId.getAmount());
            if(StrUtil.isNotEmpty(transferBill.getErrorCode())){
                emailService.send(
                        new EmailMessageDTO()
                                .setEmail("675630209@qq.com")
                                .setContent("微信转账创建失败，错误信息：" + JSON.toJSONString(transferBill))
                                .setSubject("微信转账创建失败")
                );
                emailService.send(
                        new EmailMessageDTO()
                                .setEmail("2453201633@qq.com")
                                .setContent("微信转账创建失败，错误信息：" + JSON.toJSONString(transferBill))
                                .setSubject("微信转账创建失败")
                );
                throw BaseException.of("wechatTransferError: unknown",
                        "当前提现繁忙，请稍后再试", "zh_CN");
            }
            transferOrderService.confirmTransferMethodCodeById(orderId, "wechat");
            transferOrderService.startTransfer(orderId);

            // ✅ 这里也做幂等：优先复用 existed，避免重复 new
            ThirdpartyWechatPayTransferOrder entity = existed != null ? existed : new ThirdpartyWechatPayTransferOrder();
            entity.setMchId(wechatPaySetting.getMchId());
            entity.setAmount(byId.getAmount());
            entity.setTransferOrderId(byId.getId());
            entity.setOutBillNo(orderId + "");
            entity.setPackageInfo(transferBill.getPackageInfo());
            entity.setState(transferBill.getState());
            entity.setErrorMessage(transferBill.getErrorMessage());
            entity.setErrorCode(transferBill.getErrorCode());
            entity.setRawBody(transferBill.getRawBody());

            ThirdpartyWechatPayTransferOrder save = repository.save(entity);

            if (StrUtil.isNotEmpty(transferBill.getErrorCode())) {
                throw BaseException.of("wechatTransferError: " + transferBill.getErrorCode(),
                        transferBill.getErrorMessage(), "zh_CN");
            }

            // 使用一个虚拟线程，分钟后如果没有收款，则开启撤销流程
            Thread.ofVirtual().name("ThirdpartyWechatPayTransferOrderServiceImpl-getTransferResultMiniprogram-sleep").start(() -> {
                try {
                    Thread.sleep(1000*60*3);
                    Boolean b = checkTransferSuccess(orderId);
                    if(!b){
                        // 开启撤销流程
                        cancelTransfer(orderId);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            return save;
        } finally {
            cacheUtil.unlock(lockKey);
        }
    }

    @Override
    public Boolean checkTransferSuccess(Long orderId) {
        // ✅ 永远取最新那条，避免 NonUniqueResultException
        ThirdpartyWechatPayTransferOrder latest = mustGetLatestByTransferOrderId(orderId);

        WechatTransferQuery query = new WechatTransferQuery();
        query.setWechatPaySetting(wechatPaySetting);

        WechatTransferQuery.WechatTransferQueryResult r =
                query.getWechatTransferQueryResult(latest.getOutBillNo());

        // 成功
        if ("SUCCESS".equals(r.getState())) {
            latest.setState("SUCCESS");
            repository.save(latest);
            transferOrderService.transferComplete(orderId);
            return true;
        }

        if ("CANCELLED".equals(r.getState())) {
            latest.setState("CANCELLED");
            repository.save(latest);
            transferOrderService.transferCancelComplete(orderId);
            return false;
        }

        return false;
    }

    @Override
    public Boolean cancelTransfer(Long orderId) {
        // ✅ cancel 也加锁，防止重复 cancel / 并发 cancel
        String lockKey = lockKeyOfCancel(orderId);
        Boolean locked = cacheUtil.lock(lockKey, 10, TimeUnit.SECONDS);
        if (locked == null || !locked) {
            // 正在撤销中：直接返回“已受理/处理中”
            return true;
        }
        transferOrderService.startTransferCancel(orderId);

        try {
            ThirdpartyWechatPayTransferOrder latest = mustGetLatestByTransferOrderId(orderId);

            WechatTransferCancel cancel = new WechatTransferCancel();
            cancel.setWechatPaySetting(wechatPaySetting);

            WechatTransferCancel.WechatTransferCancelResult cr =
                    cancel.cancelTransfer(latest.getOutBillNo());

            // ✅ 微信撤销状态是：CANCELING / CANCELLED（不是 CANCEL_SUCCESS）
            if ("CANCELLED".equals(cr.getState())) {
                latest.setState("CANCELLED");
                repository.save(latest);
                transferOrderService.transferCancelComplete(orderId);
                return true;
            }

            // 取消中
            if ("CANCELING".equals(cr.getState())) {
                latest.setState("CANCELING");
                repository.save(latest);
                // 这里不直接 cancelComplete，交给后续 query 收敛
                transferOrderService.startTransferCancel(orderId);
                Thread.ofVirtual().name("ThirdpartyWechatPayTransferOrderServiceImpl-cancelTransfer-sleep").start(() -> {
                    try {
                        Thread.sleep(30000);
                        // 5 秒后再查一次，看看状态有没有变
                        checkTransferSuccess(orderId);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                return true;
            }

            // 如果返回结构里有错误码（你 util 里封装了 code/message），这里按你的风格抛 BaseException
            if (StrUtil.isNotEmpty(cr.getCode())) {
                latest.setErrorCode(cr.getCode());
                latest.setErrorMessage(cr.getMessage());
                latest.setRawBody(cr.getRawBody());
                repository.save(latest);
                Thread.ofVirtual().name("ThirdpartyWechatPayTransferOrderServiceImpl-cancelTransfer-sleep").start(() -> {
                    try {
                        Thread.sleep(30000);
                        // 5 秒后再查一次，看看状态有没有变
                        checkTransferSuccess(orderId);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                throw BaseException.of("wechatTransferCancelError:" + cr.getCode(),
                        cr.getMessage(), "zh_CN");
            }

            Thread.ofVirtual().name("ThirdpartyWechatPayTransferOrderServiceImpl-cancelTransfer-sleep").start(() -> {
                try {
                    Thread.sleep(30000);
                    // 5 秒后再查一次，看看状态有没有变
                    checkTransferSuccess(orderId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });


            return false;
        } finally {
            cacheUtil.unlock(lockKey);
        }
    }

}
