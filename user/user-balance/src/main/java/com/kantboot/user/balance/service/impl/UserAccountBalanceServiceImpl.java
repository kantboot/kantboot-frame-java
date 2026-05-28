package com.kantboot.user.balance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.balance.constants.UserAccountBalanceChangeRecordStatusCodeConstants;
import com.kantboot.user.balance.dao.repository.UserAccountBalanceChangeHandleRepository;
import com.kantboot.user.balance.dao.repository.UserAccountBalanceRepository;
import com.kantboot.user.balance.domain.dto.ChangeHandleDTO;
import com.kantboot.user.balance.domain.entity.UserAccountBalance;
import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeHandle;
import com.kantboot.user.balance.exception.BalanceHandleIsLockException;
import com.kantboot.user.balance.exception.BalanceHandleNotNoProcessedException;
import com.kantboot.user.balance.exception.BalanceNotEnoughException;
import com.kantboot.user.balance.service.IUserAccountBalanceService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.event.emit.EventEmit;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserAccountBalanceServiceImpl implements IUserAccountBalanceService {


    @Resource
    private UserAccountBalanceChangeHandleRepository changeHandleRepository;

    @Resource
    private UserAccountBalanceRepository repository;

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private EventEmit eventEmit;

    @Resource
    private IUserAccountService userAccountService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = {BalanceNotEnoughException.class,BalanceNotEnoughException.class,BalanceHandleIsLockException.class})
    public UserAccountBalanceChangeHandle add(ChangeHandleDTO handleDTO) {
        String uuid = IdUtil.simpleUUID() + System.nanoTime();

        UserAccountBalanceChangeHandle changeHandle = BeanUtil.copyProperties(handleDTO, UserAccountBalanceChangeHandle.class);
        changeHandle.setUuid(uuid);
        changeHandle.setUserAccountId(handleDTO.getUserAccountId());
        changeHandle.setBalanceCode(handleDTO.getBalanceCode());
        changeHandle.setNumber(handleDTO.getNumber());
        changeHandle.setStatusCode(UserAccountBalanceChangeRecordStatusCodeConstants.NOT_PROCESSED);
        changeHandle.setLocked(false);
        changeHandle.setDescription(handleDTO.getDescription());
        changeHandleRepository.save(changeHandle);
        return handle(uuid);
    }

    @Override
    public UserAccountBalanceChangeHandle change(ChangeHandleDTO handleDTO) {
        UserAccountBalance byUserAccountIdAndBalanceCode
                = repository.findByUserAccountIdAndBalanceCode(handleDTO.getUserAccountId(), handleDTO.getBalanceCode());
        if(byUserAccountIdAndBalanceCode == null) {
            byUserAccountIdAndBalanceCode = new UserAccountBalance();
            byUserAccountIdAndBalanceCode.setBalanceCode(handleDTO.getBalanceCode());
            byUserAccountIdAndBalanceCode.setUserAccountId(handleDTO.getUserAccountId());
            byUserAccountIdAndBalanceCode.setNumber(BigDecimal.ZERO);
            repository.save(byUserAccountIdAndBalanceCode);
        }
        BigDecimal number = byUserAccountIdAndBalanceCode.getNumber();
        if(number == null){
            number = BigDecimal.ZERO;
        }
        if (number.add(handleDTO.getNumber()).compareTo(BigDecimal.ZERO) < 0) {
            log.error("用户余额不足，用户账号ID={}, 余额类型={}, 当前余额={}, 变更数值={}",
                    handleDTO.getUserAccountId(),
                    handleDTO.getBalanceCode(),
                    number,
                    handleDTO.getNumber());
            throw new BalanceNotEnoughException();
        }
        BigDecimal newNumber = handleDTO.getNumber().subtract(number);
        ChangeHandleDTO changeHandleDTO = BeanUtil.copyProperties(handleDTO, ChangeHandleDTO.class);
        changeHandleDTO.setNumber(newNumber);
        return add(changeHandleDTO);
    }

    @Override
    public UserAccountBalanceChangeHandle clear(ChangeHandleDTO changeRecord) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = {BalanceNotEnoughException.class,BalanceNotEnoughException.class,BalanceHandleIsLockException.class})
    public UserAccountBalanceChangeHandle handle(String uuid) {
        UserAccountBalanceChangeHandle handle = changeHandleRepository.findByUuid(uuid);
        if (handle == null) {
            log.error("用户余额变更处理记录不存在，uuid={}", uuid);
            throw new BalanceNotEnoughException();
        }
        if (!UserAccountBalanceChangeRecordStatusCodeConstants.NOT_PROCESSED.equals(handle.getStatusCode())) {
            log.error("用户余额变更处理记录状态不是未处理状态，uuid={}, statusCode={}", uuid, handle.getStatusCode());
            throw new BalanceHandleNotNoProcessedException();
        }
        if (!cacheUtil.lock("lockUserAccountHandleByUserAccountIdAndBalanceCode:" + handle.getUserAccountId() + "&" + handle.getBalanceCode(), 1, TimeUnit.MINUTES)) {
            handle.setLocked(true);
            changeHandleRepository.save(handle);
            log.error("用户余额变更处理加锁中，uuid={}, 用户账号ID={}, 余额类型={}", uuid, handle.getUserAccountId(), handle.getBalanceCode());
            throw  new BalanceHandleIsLockException();
        }

        Long userAccountId = handle.getUserAccountId();
        String balanceCode = handle.getBalanceCode();
        UserAccountBalance byUserAccountIdAndBalanceCode
                = repository.findByUserAccountIdAndBalanceCode(userAccountId, balanceCode);
        if (byUserAccountIdAndBalanceCode == null) {
            byUserAccountIdAndBalanceCode = new UserAccountBalance();
            byUserAccountIdAndBalanceCode.setBalanceCode(balanceCode);
            byUserAccountIdAndBalanceCode.setUserAccountId(userAccountId);
            byUserAccountIdAndBalanceCode.setNumber(BigDecimal.ZERO);
            repository.save(byUserAccountIdAndBalanceCode);
        }

        // 如果时余额减少，则需要判断余额是否足够
        if (handle.getNumber().compareTo(BigDecimal.ZERO) < 0) {
            if (byUserAccountIdAndBalanceCode.getNumber().add(handle.getNumber()).compareTo(BigDecimal.ZERO) < 0) {
                handle.setStatusCode(UserAccountBalanceChangeRecordStatusCodeConstants.FAILED);
                handle.setFailReasonCode("balanceNotEnough");
                changeHandleRepository.save(handle);
                cacheUtil.unlock("lockUserAccountHandleByUserAccountIdAndBalanceCode:" + handle.getUserAccountId() + "&" + handle.getBalanceCode());
                throw new BalanceNotEnoughException();
            }
        }

        handle.setBeforeNumber(byUserAccountIdAndBalanceCode.getNumber());
        handle.setAfterNumber(byUserAccountIdAndBalanceCode.getNumber().add(handle.getNumber()));

        // 增加余额
        repository.addNumber(
                handle.getUserAccountId(),
                handle.getBalanceCode(),
                handle.getNumber());

        handle.setStatusCode(UserAccountBalanceChangeRecordStatusCodeConstants.PROCESSED);
        UserAccountBalanceChangeHandle save = changeHandleRepository.save(handle);
        Thread.ofVirtual()
                .start(() -> {
                    UserAccountBalance userAccountBalance
                            = repository.findByUserAccountIdAndBalanceCode(userAccountId, balanceCode);
                    // 余额变化成功的事件
                    eventEmit.to("UserAccountBalance:change:" + handle.getBalanceCode(), userAccountBalance);
                    cacheUtil.unlock("lockUserAccountHandleByUserAccountIdAndBalanceCode:" + handle.getUserAccountId() + "&" + handle.getBalanceCode());
                });
        return save;
    }

    @Override
    public void handleFail(String uuid, String failReasonCode) {
    }

    @Override
    public BigDecimal getBalance(Long userAccountId, String balanceCode) {
        UserAccountBalance byUserAccountIdAndBalanceCode = repository.findByUserAccountIdAndBalanceCode(userAccountId, balanceCode);
        if(byUserAccountIdAndBalanceCode==null){
            return BigDecimal.ZERO;
        }
        BigDecimal number = byUserAccountIdAndBalanceCode.getNumber();
        if(number==null){
            return BigDecimal.ZERO;
        }
        return number;
    }

    @Override
    public List<UserAccountBalance> getBalanceList(Long userAccountId) {
        return repository.findByUserAccountId(userAccountId);
    }

    @Override
    public Map<String, BigDecimal> getBalanceMap(Long userAccountId) {
        List<UserAccountBalance> balanceList = getBalanceList(userAccountId);
        return balanceList.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                UserAccountBalance::getBalanceCode,
                                UserAccountBalance::getNumber
                        )
                );
    }

    @Override
    public Map<String, BigDecimal> getBySelf() {
        Long selfId = userAccountService.getSelfId();
        return getBalanceMap(selfId);
    }
}
