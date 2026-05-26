package com.kantboot.user.balance.timer;

import cn.hutool.core.bean.BeanUtil;
import com.kantboot.user.balance.constants.UserAccountBalanceChangeRecordStatusCodeConstants;
import com.kantboot.user.balance.dao.repository.UserAccountBalanceChangeHandleRepository;
import com.kantboot.user.balance.dao.repository.UserAccountBalanceChangeRecordRepository;
import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeHandle;
import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeRecord;
import com.kantboot.user.balance.exception.BalanceNotEnoughException;
import com.kantboot.user.balance.service.IUserAccountBalanceService;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.log.Logger;
import com.kantboot.util.timer.annotation.TimerOn;
import com.kantboot.util.timer.instruction.TimerRecordInstruction;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class UserBalanceTimer {

    @Resource
    private IUserAccountBalanceService service;

    @Resource
    private UserAccountBalanceChangeHandleRepository handleRepository;

    @TimerOn(
            code = "userBalanceScheduleHandle",
            name = "用户余额变更处理定时任务",
            time = 3000L
    )
    public void userBalanceScheduleHandle(Logger logger, TimerRecordInstruction instruction) {
        PageParam<Map<String, Object>> objectPageParam = new PageParam<>();
        objectPageParam.setPageNumber(1);
        objectPageParam.setPageSize(300);
        Page<UserAccountBalanceChangeHandle> pageByStatusCodeAndLocked = handleRepository.findPageByStatusCodeAndLocked(
                UserAccountBalanceChangeRecordStatusCodeConstants.NOT_PROCESSED, true, objectPageParam.getPageable()
        );
        List<UserAccountBalanceChangeHandle> list = pageByStatusCodeAndLocked.getContent();
        if (list.isEmpty()) {
            // 没有需要处理的记录
            return;
        }
        instruction.start();
        logger.info("开始处理用户余额变更记录，共 {} 条", list.size());
        int size = list.size();
        // 队列
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UserAccountBalanceChangeHandle handle = list.get(i);
            try {
                handle = service.handle(handle.getUuid());
                logger.info("处理用户余额变更记录，uuid: {}, 用户账号ID: {}, 变更数值:{}, 变更前数值: {}, 变更后数值: {}, 进度: {}",
                        handle.getUuid(),
                        handle.getUserAccountId(),
                        handle.getNumber(),
                        handle.getBeforeNumber(),
                        handle.getAfterNumber(),
                        (i + 1) + "/" + size);
            } catch (BalanceNotEnoughException e) {
                logger.error("处理用户余额变更，余额不足，uuid: {}, 用户账号ID: {}", handle.getUuid(), handle.getUserAccountId(),
                        (i + 1) + "/" + size);
            }
        }
        logger.info("用户余额变更记录处理完成");
        instruction.end();
    }


    @Resource
    private UserAccountBalanceChangeRecordRepository recordRepository;

    @TimerOn(
            code = "userBalanceCommitToRecordTimer",
            name = "用户余额处理提交到用户记录定时任务",
            time = 3000L
    )
    public void userBalanceCommitToRecordTimer(Logger logger, TimerRecordInstruction instruction) {
        Pageable pageable = new PageParam<>().setPageNumber(1).setPageSize(300).getPageable();
        Page<UserAccountBalanceChangeHandle> pageByStatusCodeNotAndCommitStatusCodeNot = handleRepository.findPageByStatusCodeNotAndCommitStatusCodeNot(pageable);
        List<UserAccountBalanceChangeHandle> list = pageByStatusCodeNotAndCommitStatusCodeNot.getContent();

        if (list.isEmpty()) {
            // 没有需要处理的记录
            return;
        }
        instruction.start();
        logger.info("开始处理用户余额变更记录提交到用户记录，共 {} 条", list.size());
        int size = list.size();
        // 队列
        for (int i = 0; i < list.size(); i++) {
            UserAccountBalanceChangeHandle handle = list.get(i);
            UserAccountBalanceChangeRecord byUuid = recordRepository.findByUuid(handle.getUuid());
            UserAccountBalanceChangeRecord userAccountBalanceChangeRecord
                    = BeanUtil.copyProperties(handle, UserAccountBalanceChangeRecord.class);
            userAccountBalanceChangeRecord.setId(null);
            if (byUuid != null) {
                userAccountBalanceChangeRecord.setId(byUuid.getId());
            }
            recordRepository.save(userAccountBalanceChangeRecord);
            handleRepository.updateCommitStatusCodeByUuid(handle.getUuid(), handle.getStatusCode());
            logger.info("处理用户余额变更记录提交到用户记录成功，uuid: {}, 用户账号ID: {}, 记录状态: {}, 进度: {}",
                    handle.getUuid(), handle.getUserAccountId(), userAccountBalanceChangeRecord.getStatusCode(),
                    (i + 1) + "/" + size);
        }
        instruction.end();
    }

    /**
     * 清空处理记录
     */
    @TimerOn(
            code = "userBalanceClearHandleTimer",
            name = "用户余额处理记录清理定时任务",
            time = 1000L * 60 * 60 * 24)
    public void userBalanceClearHandleTimer(Logger logger, TimerRecordInstruction instruction) {
        instruction.start();
        List<UserAccountBalanceChangeHandle> all = handleRepository.findAll();
        // 如果大于半小时，且状态非未处理，且大于30分钟，则删除
        for (int i = 0; i < all.size(); i++) {
            UserAccountBalanceChangeHandle changeHandle = all.get(i);
            Date gmtCreate = changeHandle.getGmtCreate();
            if (!UserAccountBalanceChangeRecordStatusCodeConstants.NOT_PROCESSED.equals(changeHandle.getStatusCode())
                    && (changeHandle.getStatusCode()+"").equals(changeHandle.getCommitStatusCode())
                    && (new Date().getTime() - gmtCreate.getTime() > 1000L * 60 * 30)) {
                handleRepository.delete(changeHandle);
                logger.info("清理用户余额处理记录，uuid: {}, 用户账号ID: {}, 创建时间: {}, 状态: {}, 进度: {}",
                        changeHandle.getUuid(), changeHandle.getUserAccountId(), changeHandle.getGmtCreate(),
                        changeHandle.getStatusCode(), (i + 1) + "/" + all.size());
            }
        }
        instruction.end();
    }

}
