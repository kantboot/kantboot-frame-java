package com.kantboot.user.account.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.dao.repository.UserAccountThirdpartyRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.domain.entity.UserAccountThirdparty;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountLoginService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.service.IUserAccountTokenService;
import com.kantboot.user.account.slot.UserAccountSlot;
import com.kantboot.util.crypto.password.impl.KantbootPassword;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.exception.BaseException;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户账户登录服务实现类
 * 实现了IUserAccountLoginService接口，提供多种登录方式
 */
@Service
public class UserAccountLoginServiceImpl implements IUserAccountLoginService {

    @Resource
    private UserAccountRepository repository; // 用户账户数据访问接口

    @Resource
    private IUserAccountTokenService userAccountTokenService; // 用户令牌服务接口

    @Resource
    private KantbootPassword kantbootPassword; // 密码加密与验证工具

    @Resource
    private UserAccountSlot userAccountSlot; // 用户账户相关业务槽（包含验证码发送验证等功能）

    @Resource
    private IUserAccountService userAccountService; // 用户账户基础服务接口

    @Resource
    private UserAccountThirdpartyRepository theThirdPartyRepository; // 第三方账户关联数据访问接口

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil; // HTTP请求头工具类

    /**
     * 通过用户账户ID登录
     * @param userAccountId 用户账户ID
     * @return 登录结果VO，包含令牌和用户信息
     */
    @Override
    public LoginVO loginByUserAccountId(Long userAccountId) {
        // 如果启用了管理员检查，验证该账户是否为管理员
        if(httpRequestHeaderUtil.getAdminCheckEnabled()){
            UserAccount userAccount = userAccountService.getById(userAccountId);
            if(userAccount==null){
                throw UserAccountException.ACCOUNT_NOT_EXIST; // 账号不存在异常
            }
            // 检查是否为管理员或系统管理员
            if(!Boolean.TRUE.equals(userAccount.getIsAdmin())&&!Boolean.TRUE.equals(userAccount.getIsSystemAdmin())) {
                throw BaseException.of("notAdminAccount", "非管理员账号", "zh_CN");
            }
        }

        // 根据ID查询用户账户信息
        UserAccount byId = repository.findById(userAccountId).orElse(null);
        if (byId == null) {
            // 账号不存在
            throw UserAccountException.ACCOUNT_NOT_EXIST;
        }
        // 生成访问令牌
        String token = userAccountTokenService.generateToken(byId.getId());
        // 构建并返回登录结果
        return new LoginVO()
                .setToken(token)
                .setUserAccount(byId);
    }

    /**
     * 通过用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByUsernameAndPassword(String username, String password) {
        // 根据用户名查询用户
        UserAccount byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            // 账号不存在，但出于安全考虑提示账号或密码错误
            throw UserAccountException.USERNAME_OR_PASSWORD_ERROR;
        }
        // 检查是否设置了密码
        if (byUsername.getPassword() == null) {
            // 该账号未设置密码
            throw UserAccountException.PASSWORD_NOT_SET;
        }
        // 验证密码是否正确
        if (!kantbootPassword.matches(password, byUsername.getPassword())) {
            // 密码不匹配
            throw UserAccountException.USERNAME_OR_PASSWORD_ERROR;
        }

        // 密码验证通过，通过用户ID登录
        return loginByUserAccountId(byUsername.getId());
    }

    @Override
    public LoginVO loginByUsername(String username) {
        // 根据用户名查询用户
        UserAccount byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            // 账号不存在
            throw UserAccountException.ACCOUNT_NOT_EXIST;
        }
        // 通过用户ID登录
        return loginByUserAccountId(byUsername.getId());
    }

    /**
     * 通过邮箱和密码登录
     * @param email 邮箱地址
     * @param password 密码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByEmailAndPassword(String email, String password) {
        // 根据邮箱查询用户
        UserAccount byEmail = repository.findByEmail(email);
        if (byEmail == null) {
            // 账号不存在
            throw UserAccountException.EMAIL_OR_PASSWORD_ERROR;
        }
        // 检查是否设置了密码
        if (byEmail.getPassword() == null) {
            // 该账号未设置密码
            throw UserAccountException.PASSWORD_NOT_SET;
        }
        // 验证密码是否正确
        Boolean matches = kantbootPassword.matches(password, byEmail.getPassword());
        if (!matches) {
            // 密码不匹配
            throw UserAccountException.EMAIL_OR_PASSWORD_ERROR;
        }

        // 密码验证通过，通过用户ID登录
        return loginByUserAccountId(byEmail.getId());
    }

    /**
     * 通过手机号和密码登录
     * @param phoneAreaCode 手机区号（默认86）
     * @param phone 手机号码
     * @param password 密码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByPhoneAndPassword(String phoneAreaCode, String phone, String password) {
        // 如果区号为空，设置默认区号86（中国）
        if (StrUtil.isEmpty(phoneAreaCode)) {
            phoneAreaCode = "86";
        }
        // 根据区号和手机号查询用户
        UserAccount byPhone = repository.findByPhoneAreaCodeAndPhone(phoneAreaCode, phone);
        if (byPhone == null) {
            // 账号不存在
            throw UserAccountException.PHONE_OR_PASSWORD_ERROR;
        }
        // 检查是否设置了密码
        if (byPhone.getPassword() == null) {
            // 该账号未设置密码
            throw UserAccountException.PASSWORD_NOT_SET;
        }
        // 验证密码是否正确
        if (!kantbootPassword.matches(password, byPhone.getPassword())) {
            // 密码不匹配
            throw UserAccountException.PHONE_OR_PASSWORD_ERROR;
        }

        // 密码验证通过，通过用户ID登录
        return loginByUserAccountId(byPhone.getId());
    }

    /**
     * 发送手机登录验证码
     * @param phoneAreaCode 手机区号
     * @param phone 手机号码
     */
    public void sendLoginVerificationCodeByPhone(String phoneAreaCode, String phone) {
        // 调用业务槽发送验证码
        userAccountSlot.sendLoginVerifyCodeByPhone(phoneAreaCode, phone);
    }

    /**
     * 发送邮箱登录验证码
     * @param email 邮箱地址
     */
    @Override
    public void sendLoginVerificationCodeByEmail(String email) {
        // 调用业务槽发送验证码
        userAccountSlot.sendLoginVerifyCodeByEmail(email);
    }

    /**
     * 通过手机验证码登录
     * @param phoneAreaCode 手机区号
     * @param phone 手机号码
     * @param verifyCode 验证码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByPhoneVerificationCode(String phoneAreaCode, String phone, String verifyCode) {
        // 验证验证码是否正确
        if (!userAccountSlot.matchLoginVerifyCodeByPhone(phoneAreaCode, phone, verifyCode)) {
            // 验证码错误
            throw UserAccountException.VERIFICATION_CODE_ERROR;
        }
        // 检查手机号是否已注册
        if(userAccountService.existsByPhone(phoneAreaCode, phone)){
            // 已注册，获取用户信息并登录
            UserAccount byPhone = userAccountService.getByPhone(phoneAreaCode, phone);
            return loginByUserAccountId(byPhone.getId());
        }
        // 未注册，创建新账户
        UserAccount userAccount = userAccountService.createUserAccount(new UserAccount()
                .setPhoneAreaCode(phoneAreaCode)
                .setPhone(phone));
        // 登录新创建的账户
        return loginByUserAccountId(userAccount.getId());
    }

    /**
     * 通过邮箱验证码登录
     * @param email 邮箱地址
     * @param verificationCode 验证码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByEmailAndVerificationCode(String email, String verificationCode) {
        // 验证验证码是否正确
        if (!userAccountSlot.matchLoginVerifyCodeByEmail(email, verificationCode)) {
            // 验证码错误
            throw UserAccountException.VERIFICATION_CODE_ERROR;
        }
        // 检查邮箱是否已注册
        if(userAccountService.existsByEmail(email)){
            // 已注册，获取用户信息并生成令牌
            UserAccount byEmail = userAccountService.getByEmail(email);
            String token = userAccountTokenService.generateToken(byEmail.getId());
            return new LoginVO()
                    .setToken(token)
                    .setUserAccount(byEmail);
        }
        // 未注册，创建新账户
        UserAccount userAccount = userAccountService.createUserAccount(new UserAccount()
                .setEmail(email));
        // 登录新创建的账户
        return loginByUserAccountId(userAccount.getId());
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        // 移除当前用户的令牌
        userAccountTokenService.removeTokenBySelf();
    }

    /**
     * 检查是否已登录
     * @return 是否已登录
     */
    @Override
    public boolean checkLogin() {
        // 检查当前用户ID是否存在
        return userAccountTokenService.getUserAccountIdBySelf() != null;
    }

    /**
     * 判断是否已登录
     * @return 是否已登录
     */
    @Override
    public boolean isLogin() {
        // 检查当前用户是否已登录
        return userAccountTokenService.isLogin();
    }

    /**
     * 通过手机号登录（无密码，用于一键登录场景）
     * @param phoneAreaCode 手机区号
     * @param phone 手机号码
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByPhone(String phoneAreaCode, String phone) {
        // 根据手机号查询用户
        UserAccount byPhone = repository.findByPhoneAreaCodeAndPhone(phoneAreaCode, phone);
        if (byPhone == null) {
            // 用户不存在，创建新账户
            userAccountService.createUserAccount(new UserAccount()
                    .setPhoneAreaCode(phoneAreaCode)
                    .setPhone(phone));
            // 获取新创建的用户
            byPhone = userAccountService.getByPhone(phoneAreaCode, phone);
        }
        // 通过用户ID登录
        return loginByUserAccountId(byPhone.getId());
    }

    /**
     * 通过邮箱登录（无密码，用于一键登录场景）
     * @param email 邮箱地址
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByEmail(String email) {
        // 根据邮箱查询用户
        UserAccount byEmail = repository.findByEmail(email);
        if (byEmail == null) {
            // 用户不存在，创建新账户
            userAccountService.createUserAccount(new UserAccount()
                    .setEmail(email));
            // 获取新创建的用户
            byEmail = userAccountService.getByEmail(email);
        }
        // 通过用户ID登录
        return loginByUserAccountId(byEmail.getId());
    }

    /**
     * 通过第三方账户登录
     * @param thirdpartyCode 第三方平台代码
     * @param key 第三方账户键名
     * @param value 第三方账户键值
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByThirdparty(String thirdpartyCode, String key, String value) {
        // 参数校验
        if (StringUtils.isBlank(thirdpartyCode) || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            // 参数错误
            throw UserAccountException.PARAMETER_ERROR;
        }
        // 查询第三方账户关联信息
        UserAccountThirdparty byThirdPartyCodeAndKeyAndValue = theThirdPartyRepository.findByThirdpartyCodeAndKeyAndValue(thirdpartyCode, key, value);
        if (byThirdPartyCodeAndKeyAndValue != null) {
            // 关联信息存在，获取对应用户
            UserAccount byId = repository.findById(byThirdPartyCodeAndKeyAndValue.getUserAccountId()).orElse(null);
            if (byId != null) {
                // 用户存在，通过用户ID登录
                return loginByUserAccountId(byId.getId());
            }
        }
        // 关联信息不存在，创建新用户
        UserAccount userAccount = userAccountService.createUserAccount(new UserAccount());
        // 创建第三方账户关联
        UserAccountThirdparty userAccountThirdParty = new UserAccountThirdparty()
                .setUserAccountId(userAccount.getId())
                .setThirdpartyCode(thirdpartyCode)
                .setKey(key)
                .setValue(value);
        // 保存关联信息
        theThirdPartyRepository.save(userAccountThirdParty);
        // 登录新创建的用户
        return loginByUserAccountId(userAccount.getId());
    }

    /**
     * 通过第三方账户和邮箱登录（用于绑定邮箱场景）
     * @param thirdPartyCode 第三方平台代码
     * @param key 第三方账户键名
     * @param value 第三方账户键值
     * @param email 邮箱地址
     * @return 登录结果VO
     */
    @Override
    public LoginVO loginByThirdpartyAndEmail(String thirdPartyCode, String key, String value, String email) {
        // 参数校验
        if (StringUtils.isBlank(thirdPartyCode) || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            // 参数错误
            throw UserAccountException.PARAMETER_ERROR;
        }
        // 根据邮箱查询用户
        UserAccount byEmail = userAccountService.getByEmail(email);
        // 查询第三方账户关联信息
        UserAccountThirdparty byThirdPartyCodeAndKeyAndValue = theThirdPartyRepository.findByThirdpartyCodeAndKeyAndValue(thirdPartyCode, key, value);

        if (byThirdPartyCodeAndKeyAndValue != null) {
            // 关联信息存在
            if(byEmail == null){
                // 邮箱未注册，补全邮箱信息
                UserAccount userAccount = repository.findById(byThirdPartyCodeAndKeyAndValue.getUserAccountId()).orElse(null);
                if(userAccount!=null){
                    userAccount.setEmail(email);
                    repository.save(userAccount);
                }
            }
            // 直接登录已绑定的账户
            return loginByUserAccountId(byThirdPartyCodeAndKeyAndValue.getUserAccountId());
        }

        // 关联信息不存在，但邮箱已注册
        if (byEmail != null) {
            Long id = byEmail.getId();
            // 创建新的第三方账户关联
            UserAccountThirdparty userAccountThirdParty = new UserAccountThirdparty()
                    .setUserAccountId(id)
                    .setThirdpartyCode(thirdPartyCode)
                    .setKey(key)
                    .setValue(value);
            // 保存关联信息
            theThirdPartyRepository.save(userAccountThirdParty);
            // 登录账户
            return loginByUserAccountId(id);
        }

        // 关联信息和邮箱都不存在，创建新账户
        LoginVO loginVO = loginByThirdparty(thirdPartyCode, key, value);
        UserAccount userAccount = loginVO.getUserAccount();
        // 设置邮箱信息
        userAccount.setEmail(email);
        repository.save(userAccount);
        return loginVO;
    }
}