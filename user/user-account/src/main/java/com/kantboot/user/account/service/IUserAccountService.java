package com.kantboot.user.account.service;

import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.util.jpa.param.PageParam;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 用户账户服务
 */
public interface IUserAccountService {

    /**
     * 查询所有管理员账号
     */
    List<UserAccount> getAllAdmin();


    Page<UserAccount> search(PageParam<Map<String,String>> pageParam);

    /**
     * 获取所有用户数量
     */
    Long getTotal();

    /**
     * 根据用户名前缀获取用户列表
     */
    List<UserAccount> getByUsernamePrefix(String prefix);

    /**
     * 根据用户名前缀获取用户数量
     */
    Long getCountByUsernamePrefix(String prefix);

    /**
     * 加密密码
     */
    String encryptPassword(String password);

    /**
     * 修改基本信息
     */
    UserAccount editCommonInfo(UserAccount userAccount);

    UserAccount editCommonInfoSelf(UserAccount userAccount);

    /**
     * 创建新用户账号
     */
    UserAccount createUserAccount(UserAccount userAccount);

    /**
     * 根据用户账号ID获取用户账号信息
     */
    UserAccount getById(Long id);

    /**
     * 根据用户账号IDS获取用户账号信息列表
     */
    List<UserAccount> getByIds(List<Long> ids);

    /**
     * 获取自身信息的ID
     */
    Long getSelfId();

    /**
     * 获取自身信息的ID（不抛出异常）
     */
    Long getSelfIdNoThrow();

    /**
     * 获取自身信息
     */
    UserAccount getSelf();

    /**
     * 保存自身信息
     */
    UserAccount saveSelf(UserAccount userAccount);

    /**
     * 修改密码
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * 修改密码不需要原密码
     */
    void changePasswordNoHasOldPassword(String newPassword);

    /**
     * 判断是否存在手机号
     */
    boolean existsByPhone(String phoneAreaCode, String phone);

    /**
     * 判断是否存在邮箱
     */
    boolean existsByEmail(String email);

    /**
     * 判断是否存在用户名
     */
    boolean existsByUsername(String username);

    /**
     * 根据用户名获取用户
     */
    UserAccount getByUsername(String username);

    /**
     * 不抛出异常的根据用户名获取用户
     */
    UserAccount getByUsernameNoThrow(String username);

    /**
     * 根据用户名模糊查询
     */
    List<UserAccount> getByUsernameVague(String username);

    /**
     * 根据邮箱获取用户
     */
    UserAccount getByEmail(String email);

    /**
     * 根据手机号获取用户
     */
    UserAccount getByPhone(String phoneAreaCode, String phone);

    /**
     * 根据手机号获取用户
     */
    UserAccount save(UserAccount userAccount);

    /**
     * 是否有密码
     */
    boolean isHasPassword(Long userAccountId);

    /**
     * 用户自身查看是否有密码
     */
    boolean isHasPasswordSelf();

    /**
     * 设置密码
     * 当密码为空时可用
     */
    void setPassword(Long userAccountId, String password);

    /**
     * 用户自身设置密码
     * 当密码为空时可用
     */
    void setPasswordSelf(String password);

    /**
     * 管理员设置密码
     * 不判断密码是否为空
     */
    void setPasswordAdmin(Long userAccountId, String password);

    /**
     * 获取所有可访问的URI
     */
    List<String> getAllAccessibleUri(Long userAccountId);

    /**
     * 用户自身获取所有可访问的URI
     */
    List<String> getAllAccessibleUriSelf();

    /**
     * 修改用户名
     */
    void changeUsername(Long userAccountId,String username);

    /**
     * 获取第三方账号信息
     */
    String getThirdPartyValue(Long userAccountId, String thirdpartyCode,String key);

}
