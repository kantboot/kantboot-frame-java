package com.kantboot.user.account.web.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.crypto.password.impl.KantbootPassword;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.exception.BaseException;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AuthInit(name = "用户账号管理",description = "用户账号管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/admin/userAccount")
public class UserAccountControllerOfAdmin extends BaseAdminController<UserAccount,Long> {

    @Resource
    private UserAccountRepository repository;

    @Resource
    private KantbootPassword kantbootPassword;

    @Resource
    private IUserAccountService service;

    @AuthInit(name="搜索",description = "搜索", sourceLanguageCode = "zh_CN")
    @RequestMapping("/search")
    public RestResult<Object> search(@RequestBody PageParam<Map<String,String>> pageParam) {
        return RestResult.success(service.search(pageParam), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @Override
    @AuthInit(name = "保存（重写了通用后台管理）",description = "重写了通用后台管理", sourceLanguageCode = "zh_CN")
    @RequestMapping("/save")
    public RestResult<Object> save(@RequestBody UserAccount entity) {
        // 如果用户名不为空
        if(entity.getUsername()!=null){
            // 判断用户名是否存在
            UserAccount byUsername = repository.findByUsername(entity.getUsername());
            if (byUsername != null && !byUsername.getId().equals(entity.getId())) {
                throw BaseException.of("usernameIsExist", "用户名已存在");
            }
        }
        // 如果密码不为空与ID不为空
        if(entity.getPassword()!=null && entity.getId()!=null){
            // 根据ID查询用户
            UserAccount byId = repository.findById(entity.getId()).orElse(null);
            // 如果用户不存在
            if(byId==null){
                throw BaseException.of("userNotExist", "用户不存在");
            }
            // 如果密码不相等
            if(!byId.getPassword().equals(entity.getPassword())){
                String password = entity.getPassword();
                String encrypt = kantbootPassword.encrypt(password.trim());
                entity.setPassword(encrypt);
            }
        }
        // 如果密码不为空与ID为空
        if(entity.getPassword()!=null && entity.getId()==null){
            String password = entity.getPassword();
            String encrypt = kantbootPassword.encrypt(password.trim());
            entity.setPassword(encrypt);
        }
        // 如果ID不为空，就根据ID搜索
        if(entity.getId()!=null){
            UserAccount byId = repository.findById(entity.getId()).orElse(null);
            if(byId==null){
                throw BaseException.of("userNotExist", "用户不存在");
            }
            // 获取所有entity非空字段，只修改非空字段
            // 使用反射
            try {
                java.lang.reflect.Field[] fields = UserAccount.class.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null) {
                        field.set(byId, value);
                    }
                }
                System.out.println("JSON.toJSONString(byId)="+JSON.toJSONString(byId));
                repository.save(byId);
            } catch (IllegalAccessException e) {
                throw BaseException.of("reflectionError", "反射错误");
            }
            return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
        }

        service.save(entity);
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    /**
     * 根据邮箱获取
     */
    @AuthInit(name = "根据邮箱获取",description = "根据邮箱获取", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getByEmail")
    public RestResult<Object> getByEmail(@RequestParam("email") String email) {
        return RestResult.success(service.getByEmail(email), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 设置密码
     */
    @AuthInit(name = "设置密码",description = "设置密码", sourceLanguageCode = "zh_CN")
    @RequestMapping("/setPassword")
    public RestResult<Void> setPassword(
            @RequestParam("userAccountId") Long userAccountId,
            @RequestParam("password") String password
    ){
        service.setPassword(userAccountId, password);
        return RestResult.success(null, CommonSuccessStateConsts.SET_SUCCESS);
    }


}
