package com.kantboot.user.account.service.impl;

import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.dao.repository.UserAccountThirdpartyRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.domain.entity.UserAccountThirdparty;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountBindService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.slot.UserAccountSlot;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAccountBindServiceImpl implements IUserAccountBindService {

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private UserAccountRepository repository;

    @Resource
    private UserAccountSlot slot;

    @Resource
    private UserAccountThirdpartyRepository thirdPartyRepository;


    @Override
    public void skipBind() {
        UserAccount userAccount = userAccountService.getSelf();
        userAccount.setIsTemporary(false);
        repository.save(userAccount);
    }

    @Override
    public void sendVerificationCodeByEmail(String email) {
        if (userAccountService.existsByEmail(email)) {
            throw UserAccountException.EMAIL_EXIST;
        }
        slot.sendBindVerifyCodeByEmail(email);
    }

    @Override
    public void sendVerificationCodeByPhone(String phoneAreaCode, String phone) {
        if (userAccountService.existsByPhone(phoneAreaCode, phone)) {
            throw UserAccountException.PHONE_EXIST;
        }
        slot.sendBindVerifyCodeByPhone(phoneAreaCode, phone);
    }

    @Override
    public void bindByEmailAndVerificationCode(String email, String verificationCode) {
        if (userAccountService.existsByEmail(email)) {
            throw UserAccountException.EMAIL_EXIST;
        }
        if (!slot.matchBindVerifyCodeByEmail(email, verificationCode)) {
            throw UserAccountException.VERIFICATION_CODE_ERROR;
        }
        UserAccount userAccount = userAccountService.getSelf();
        userAccount.setEmail(email);
        repository.save(userAccount);
    }

    @Override
    public void bindByPhoneAndVerificationCode(String phoneAreaCode, String phone, String verificationCode) {
        if (userAccountService.existsByPhone(phoneAreaCode, phone)) {
            throw UserAccountException.PHONE_EXIST;
        }
        if (!slot.matchBindVerifyCodeByPhone(phoneAreaCode, phone, verificationCode)) {
            throw UserAccountException.VERIFICATION_CODE_ERROR;
        }
        UserAccount userAccount = userAccountService.getSelf();
        userAccount.setPhoneAreaCode(phoneAreaCode);
        userAccount.setPhone(phone);
        repository.save(userAccount);
    }

    /**
     * 绑定第三方账号到当前用户账户
     *
     * @param thirdPartyCode 第三方平台标识码（如：wechat/alipay等）
     * @param key 第三方账号关联键（如：open_id/union_id等）
     * @param value 第三方账号唯一标识值
     * @throws UserAccountException 当检测到重复绑定时抛出 THIRD_PARTY_BIND_EXIST 异常
     *
     * 实现逻辑：
     * 1. 先检查当前用户是否已绑定相同平台相同凭证
     * 2. 再全局检查该凭证是否已被其他用户绑定
     * 3. 通过校验后创建新的绑定关系
     */
    @Override
    public void bindByThirdparty(String thirdPartyCode, String key, String value) {
        // 获取当前的用户
        UserAccount userAccount = userAccountService.getSelf();

        // 检查当前用户是否已存在相同绑定（用户维度去重）
        UserAccountThirdparty byUserAccountId =
                thirdPartyRepository.findByUserAccountIdAndThirdpartyCodeAndKeyAndValue(userAccount.getId(), thirdPartyCode, key, value);
        if (byUserAccountId != null&&!byUserAccountId.getId().equals(userAccount.getId())){
//            throw UserAccountException.THIRD_PARTY_BIND_EXIST;
            // 将原先的绑定关系删除，允许重新绑定
            thirdPartyRepository.delete(byUserAccountId);
        }

        // 全局检查该凭证是否已被使用（系统维度去重）
        UserAccountThirdparty byThirdPartyCodeAndKeyAndValue =
                thirdPartyRepository.findByThirdpartyCodeAndKeyAndValue(thirdPartyCode, key, value);
        if (byThirdPartyCodeAndKeyAndValue != null) {
            throw UserAccountException.THIRD_PARTY_BIND_EXIST;
        }

        // 创建并持久化新的绑定关系
        UserAccountThirdparty userAccountThirdParty = new UserAccountThirdparty()
                .setUserAccountId(userAccount.getId())
                .setThirdpartyCode(thirdPartyCode)
                .setKey(key)
                .setValue(value);
        thirdPartyRepository.save(userAccountThirdParty);
    }

    @Override
    public void bindByThirdparty(Long userAccountId, String thirdPartyCode, String key, String value) {
        // 获取当前的用户
        UserAccount userAccount = userAccountService.getById(userAccountId);

        // 检查当前用户是否已存在相同绑定（用户维度去重）
        UserAccountThirdparty byUserAccountId =
                thirdPartyRepository.findByUserAccountIdAndThirdpartyCodeAndKeyAndValue(userAccount.getId(), thirdPartyCode, key, value);
        if (byUserAccountId != null&&!byUserAccountId.getId().equals(userAccount.getId())){
//            throw UserAccountException.THIRD_PARTY_BIND_EXIST;
            // 将原先的绑定关系删除，允许重新绑定
            thirdPartyRepository.delete(byUserAccountId);
        }

        // 全局检查该凭证是否已被使用（系统维度去重）
        UserAccountThirdparty byThirdPartyCodeAndKeyAndValue =
                thirdPartyRepository.findByThirdpartyCodeAndKeyAndValue(thirdPartyCode, key, value);
        if (byThirdPartyCodeAndKeyAndValue != null&& !byThirdPartyCodeAndKeyAndValue.getUserAccountId().equals(userAccount.getId())) {
//            throw UserAccountException.THIRD_PARTY_BIND_EXIST;
            // 蒋原先的绑定关系删除，允许重新绑定
            thirdPartyRepository.delete(byThirdPartyCodeAndKeyAndValue);
        }

        // 创建并持久化新的绑定关系
        UserAccountThirdparty userAccountThirdParty = new UserAccountThirdparty()
                .setUserAccountId(userAccount.getId())
                .setThirdpartyCode(thirdPartyCode)
                .setKey(key)
                .setValue(value);
        thirdPartyRepository.save(userAccountThirdParty);
    }

    @Override
    public void bindByThirdpartyAndEmail(String thirdPartyCode, String key, String value, String email) {
        bindByThirdparty(thirdPartyCode, key, value);
        UserAccount userAccount = userAccountService.getSelf();
        if (!userAccountService.existsByEmail(email)) {
            userAccount.setEmail(email);
        }
        repository.save(userAccount);
    }

    @Override
    public void bindByPhone(String phoneAreaCode, String phone) {
        // TODO
    }
}
