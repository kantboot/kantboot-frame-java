package com.kantboot.user.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.system.auth.service.ISysAuthRoleService;
import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.dao.repository.UserAccountThirdpartyRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.domain.entity.UserAccountThirdparty;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.service.IUserAccountTokenService;
import com.kantboot.user.account.util.UserAccountSaveCheckUtil;
import com.kantboot.util.crypto.password.impl.KantbootPassword;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserAccountServiceImpl implements IUserAccountService {

    @Resource
    private UserAccountRepository repository;

    @Resource
    private KantbootPassword kantbootPassword;

    @Resource
    private UserAccountSaveCheckUtil userAccountSaveCheckUtil;

    @Resource
    private IUserAccountTokenService userAccountTokenService;

    @Resource
    private EventEmit eventEmit;

    @Resource
    private ISysAuthRoleService sysAuthRoleService;

    @Resource
    private UserAccountThirdpartyRepository userAccountThirdpartyRepository;

    @Override
    public List<UserAccount> getAllAdmin() {
        return repository.findByIsAdmin(true);
    }

    @Override
    public Page<UserAccount> search(PageParam<Map<String, String>> pageParam) {
        return repository.search(pageParam.getData().get("keyword"), pageParam.getPageable());
    }

    @Override
    public Long getTotal() {
        return repository.count();
    }

    @Override
    public List<UserAccount> getByUsernamePrefix(String prefix) {
        return repository.findByUsernamePrefix(prefix);
    }

    @Override
    public Long getCountByUsernamePrefix(String prefix) {
        return repository.countByUsernamePrefix(prefix);
    }

    /**
     * 密码加密
     * 必须在校验密码之后调用
     */
    @Override
    public String encryptPassword(String password) {
        // 密码加密
        return kantbootPassword.encrypt(password);
    }

    @Override
    public UserAccount editCommonInfo(UserAccount userAccount) {
        UserAccount byId = getById(userAccount.getId());
        Long fileIdOfAvatar = userAccount.getFileIdOfAvatar();
        String nickname = userAccount.getNickname();
        String introduction = userAccount.getIntroduction();
        String genderCode = userAccount.getGenderCode();
        Date gmtBirthday = userAccount.getGmtBirthday();
        if(fileIdOfAvatar!=null){
            byId.setFileIdOfAvatar(fileIdOfAvatar);
        }
        if(StrUtil.isNotEmpty(nickname)){
            byId.setNickname(nickname);
        }
        if(StrUtil.isNotEmpty(introduction)){
            byId.setIntroduction(introduction);
        }
        if(StrUtil.isNotEmpty(genderCode)){
            byId.setGenderCode(genderCode);
        }
        if(gmtBirthday!=null){
            byId.setGmtBirthday(gmtBirthday);
        }
        return save(byId);
    }

    @Override
    public UserAccount editCommonInfoSelf(UserAccount userAccount) {
        return editCommonInfo(userAccount.setId(getSelfId()));
    }

    /**
     * 创建新用户账号
     */
    @Override
    public UserAccount createUserAccount(UserAccount userAccount) {
        // 设置为临时账号
        // 如果临时账号为空，则设置为true
        if (userAccount.getIsTemporary() == null) {
            userAccount.setIsTemporary(true);
        }
        // 如果userAccount的isAdmin为空，则设置为false
        if (userAccount.getIsAdmin() == null) {
            userAccount.setIsAdmin(false);
        }
        // 如果userAccount的isSystemAdmin为空，则设置为false
        if (userAccount.getIsSystemAdmin() == null) {
            userAccount.setIsSystemAdmin(false);
        }
        // 校验用户账号
        userAccountSaveCheckUtil.check(userAccount);
        // 密码加密
        userAccount.setPassword(encryptPassword(userAccount.getPassword()));
        // 保存用户账号
        UserAccount save = repository.save(userAccount);
        eventEmit.to("UserAccount:createUserAccount",save);
        UserAccount result = BeanUtil.copyProperties(save, UserAccount.class);
        // 将密码置空
        result.setPassword(null);
        return result;
    }

    @Override
    public UserAccount getById(Long id) {
        UserAccount userAccount = repository.findById(id).orElse(null);
        if (userAccount == null) {
            // 用户账号不存在
            throw UserAccountException.NOT_EXIST;
        }
        return userAccount;
    }

    @Override
    public List<UserAccount> getByIds(List<Long> ids) {
        return repository.getByIds(ids);
    }

    @Override
    public Long getSelfId() {
        Long userAccountId = userAccountTokenService.getUserAccountIdBySelf();
        if (userAccountId == null) {
            // 账号未登录
            throw UserAccountException.NOT_LOGIN;
        }
        return userAccountId;
    }

    @Override
    public Long getSelfIdNoThrow() {
        try {
            return getSelfId();
        } catch (BaseException e) {
            return null;
        }
    }

    @Override
    public UserAccount getSelf() {
        Long userAccountId = userAccountTokenService.getUserAccountIdBySelf();
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            // 账号未登录
            throw UserAccountException.NOT_LOGIN;
        }
        UserAccount result = BeanUtil.copyProperties(userAccount, UserAccount.class);
        // 将密码置空
        result.setPassword(null);
        return result;
    }


    @Override
    public UserAccount saveSelf(UserAccount userAccount) {
        // 获取当前用户
        Long selfId = getSelfId();
        UserAccount userAccountInDb = repository.findById(selfId).orElse(null);
        if (userAccountInDb == null) {
            // 账号未登录
            throw UserAccountException.NOT_LOGIN;
        }
        // 设置头像
        userAccountInDb.setFileIdOfAvatar(userAccount.getFileIdOfAvatar());
        // 设置昵称
        userAccountInDb.setNickname(userAccount.getNickname());
        // 设置自我介绍
        userAccountInDb.setIntroduction(userAccount.getIntroduction());
        // 保存用户账号
        UserAccount save = save(userAccountInDb);
        UserAccount result = BeanUtil.copyProperties(save, UserAccount.class);
        // 将密码置空
        result.setPassword(null);
        return result;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // 获取当前用户
        Long selfId = getSelfId();
        UserAccount userAccount = repository.findById(selfId).orElse(null);
        if (userAccount == null) {
            // 账号未登录
            throw UserAccountException.NOT_LOGIN;
        }
        // 校验新密码
        userAccountSaveCheckUtil.checkPassword(newPassword);

        // 校验密码
        if (!kantbootPassword.matches(oldPassword, userAccount.getPassword())) {
            // 密码错误
            throw UserAccountException.OLD_PASSWORD_ERROR;
        }
        // 密码加密
        userAccount.setPassword(encryptPassword(newPassword));
        // 保存用户账号
        repository.save(userAccount);
    }

    @Override
    public void changePasswordNoHasOldPassword(String newPassword) {
        // 获取当前用户
        Long selfId = getSelfId();
        UserAccount userAccount = repository.findById(selfId).orElse(null);
        if (userAccount == null) {
            // 账号未登录
            throw UserAccountException.NOT_LOGIN;
        }
        // 校验新密码
        userAccountSaveCheckUtil.checkPassword(newPassword);
        // 密码加密
        userAccount.setPassword(encryptPassword(newPassword));
        // 保存用户账号
        repository.save(userAccount);
    }

    @Override
    public boolean existsByPhone(String phoneAreaCode, String phone) {
        return repository.existsByPhoneAreaCodeAndPhone(phoneAreaCode, phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public UserAccount getByEmail(String email) {
        UserAccount byEmail = repository.findByEmail(email);
        if (byEmail == null) {
            // 用户账号不存在
            throw UserAccountException.NOT_EXIST;
        }
        return byEmail;
    }

    @Override
    public UserAccount getByUsername(String username) {
        UserAccount byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            // 用户账号不存在
            throw UserAccountException.NOT_EXIST;
        }
        return byUsername;
    }

    @Override
    public UserAccount getByUsernameNoThrow(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<UserAccount> getByUsernameVague(String username) {
        return repository.findByUsernameVague(username);
    }

    @Override
    public UserAccount getByPhone(String phoneAreaCode, String phone) {
        UserAccount byPhone = repository.findByPhoneAreaCodeAndPhone(phoneAreaCode, phone);
        if (byPhone == null) {
            // 用户账号不存在
            throw UserAccountException.NOT_EXIST;
        }
        return byPhone;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        if (userAccount.getId() == null) {
            // 创建新用户账号
            return createUserAccount(userAccount);
        }

        // 获取对应的实体
        UserAccount dbEntity = getById(userAccount.getId());
        if(dbEntity!=null){
            // 如果不为空，则进行属性拷贝，如果entity中的值为空，则把空值赋值成dbEntity中的值
            JSONObject dbEntityJson = JSON.parseObject(JSON.toJSONString(dbEntity));
            JSONObject entityJson = JSON.parseObject(JSON.toJSONString(userAccount));
            for (String key : dbEntityJson.keySet()) {
                if(entityJson.get(key)==null){
                    entityJson.put(key,dbEntityJson.get(key));
                }
            }
            userAccount = JSON.parseObject(entityJson.toJSONString(), UserAccount.class);
        }

        // 根据id获取用户账号
        UserAccount userAccountInDb = repository.findById(userAccount.getId()).orElse(null);
        if (userAccountInDb == null) {
            // 用户账号不存在
            throw UserAccountException.NOT_EXIST;
        }
        // 如果传入的密码为 null，则保留数据库中的密码
        if (userAccount.getPassword() == null) {
            userAccount.setPassword(userAccountInDb.getPassword());
        }
        userAccount.setIsSystemAdmin(userAccountInDb.getIsSystemAdmin());
        userAccount.setIsAdmin(userAccountInDb.getIsAdmin());
        userAccount.setGmtCreate(userAccountInDb.getGmtCreate());
        // 获取此次的手机号
        String phone = userAccount.getPhoneAreaCode() + userAccount.getPhone();
        // 获取inDb的手机号
        String phoneInDb = userAccountInDb.getPhoneAreaCode() + userAccountInDb.getPhone();
        if (!StrUtil.isEmpty(phone) && !phone.equals(phoneInDb)) {
            // 如果手机号码不为空，则校验手机号码
            userAccountSaveCheckUtil.checkPhone(userAccount);
        }
        // 获取此次的邮箱
        String email = userAccount.getEmail();
        if (!StrUtil.isEmpty(email) && !email.equals(userAccountInDb.getEmail())) {
            // 如果邮箱不为空，则校验邮箱
            userAccountSaveCheckUtil.checkEmail(userAccount);
        }
        if (userAccount.getUsername() == null) {
            // 如果用户名不为空，则校验用户名
            userAccountSaveCheckUtil.checkUsername(userAccount);
        }
        UserAccount save = repository.save(userAccount);
        eventEmit.to("userAccount:change", save);
        return save;
    }

    @Override
    public boolean isHasPassword(Long userAccountId) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount != null) {
            return StrUtil.isNotEmpty(userAccount.getPassword());
        }
        return false;
    }

    @Override
    public boolean isHasPasswordSelf() {
        Long selfId = getSelfId();
        return isHasPassword(selfId);
    }

    @Override
    public void setPassword(Long userAccountId, String password) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount != null) {
            // 密码加密
            if (StringUtils.hasText(password)) {
                userAccountSaveCheckUtil.checkPassword(password);
                String encrypt = encryptPassword(password);
                userAccount.setPassword(encrypt);
            } else {
//                userAccount.setPassword(null);
            }
            repository.save(userAccount);
            return;
        }
        throw UserAccountException.NOT_EXIST;
    }

    @Override
    public void setPasswordSelf(String password) {
        Long selfId = getSelfId();
        setPassword(selfId, password);
    }

    @Override
    public void setPasswordAdmin(Long userAccountId, String password) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount != null) {
            // 密码加密
            String encrypt = encryptPassword(password);
            userAccount.setPassword(encrypt);
            userAccount.setIsAdmin(true);
            repository.save(userAccount);
        }
        throw UserAccountException.NOT_EXIST;
    }

    @Override
    public List<String> getAllAccessibleUri(Long userAccountId) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            return List.of();
        }
        return sysAuthRoleService.getUrisByIds(userAccount.getRoleIds());
    }

    @Override
    public List<String> getAllAccessibleUriSelf() {
        Long selfId = getSelfId();
        return getAllAccessibleUri(selfId);
    }

    @Override
    public void changeUsername(Long userAccountId, String username) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            throw UserAccountException.NOT_EXIST;
        }
        UserAccount byUsername = repository.findByUsername(username);
        if (byUsername != null && !byUsername.getId().equals(userAccountId)) {
            throw UserAccountException.USERNAME_EXIST;
        }
        userAccount.setUsername(username);
        repository.save(userAccount);
    }

    @Override
    public String getThirdPartyValue(Long userAccountId, String thirdpartyCode, String key) {
        UserAccountThirdparty userAccountThirdparty = userAccountThirdpartyRepository
                .findByUserAccountIdAndThirdpartyCodeAndKey(
                        userAccountId, thirdpartyCode, key);
        if (userAccountThirdparty != null) {
            return userAccountThirdparty.getValue();
        }
        return null;
    }
}
