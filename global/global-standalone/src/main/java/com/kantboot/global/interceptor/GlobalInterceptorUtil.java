package com.kantboot.global.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.global.util.GlobalWebControllerLogUtil;
import com.kantboot.system.auth.domain.entity.SysAuthUri;
import com.kantboot.system.auth.service.ISysAuthUriService;
import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.tool.ip.domain.entity.ToolIp;
import com.kantboot.tool.ip.service.IToolIpService;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.service.IUserAccountLoginService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.location.domain.entity.UserAccountLocation;
import com.kantboot.user.location.service.IUserAccountLocationService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 全局过滤器工具类
 * Global filter utility class
 */
@Slf4j
@Component
public class GlobalInterceptorUtil {

    @Resource
    private ISysAuthUriService authUriService;

    @Resource
    private IUserAccountLoginService userAccountLoginService;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IUserAccountLocationService userAccountLocationService;

    @Resource
    private GlobalWebControllerLogUtil globalWebControllerLogUtil;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private IToolAreaService toolAreaService;

    @Resource
    private IToolIpService toolIpService;

    @Resource
    private CacheUtil cacheUtil;

    /**
     * 检测是否放行
     * Check if it is released
     * @return 1代表放行，2代表未登录，3代表没有权限
     */
    public byte checkPass(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        SysAuthUri byUri = authUriService.getByUri(uri);
        // 如果不存在该URI，则放行
        if (byUri == null) {
            // 提示非法请求
            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().println(JSON.toJSONString(new BaseException().exceptionHandler(
                        BaseException.of("illegalRequest", "非法请求", "zh_CN")
                )));
                globalWebControllerLogUtil.addWebControllerLog(null,"illegalRequest");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 2;
        }

        // 检测是byUri的noNeedLogin属性是否为true
        if (Boolean.TRUE.equals(byUri.getNoNeedLogin())) {
            return 1;
        }

        // 检测是byUri的allPass属性是否为true
        boolean allPass = Boolean.TRUE.equals(byUri.getAllPass());
        // 获取登录状态
        boolean isLogin = userAccountLoginService.isLogin();
        if(!isLogin){
            // 提示未登录
            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().println(JSON.toJSONString(new BaseException().exceptionHandler(
                        BaseException.of("notLogin", "未登录", "zh_CN")
                )));
                globalWebControllerLogUtil.addWebControllerLog(null,"notLogin");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 2;
        }
        // 如果登录了则放行
        if (allPass) {
            return 1;
        }

        UserAccount self = userAccountService.getSelf();
        Boolean isSystemAdmin = self.getIsSystemAdmin();
        // 如果是系统管理员则放行
        if (Boolean.TRUE.equals(isSystemAdmin)) {
            return 1;
        }
        List<String> allAccessibleUri = userAccountService.getAllAccessibleUri(self.getId());
        // 如果包含该URI则放行
        if (allAccessibleUri.contains(uri)) {
            return 1;
        }
        return 3;
    }

    public void getLocation(){
        String ip = httpRequestHeaderUtil.getIp();
        String byIpSelfStr = cacheUtil.get("ip:" + ip);
        ToolIp byIpSelf = null;
        if(StrUtil.isNotEmpty(byIpSelfStr)){
            byIpSelf = JSON.parseObject(byIpSelfStr, ToolIp.class);
        } else{
            // 获取IP地址
            byIpSelf = toolIpService.getByIpSelf();
            cacheUtil.setEx("ip:" + ip, JSON.toJSONString(byIpSelf), 30, TimeUnit.DAYS);
        }

        Long selfId = userAccountService.getSelfId();
        if(selfId==null){
            return;
        }

        // 获取经度
        BigDecimal longitude = httpRequestHeaderUtil.getLongitude();
        // 获取纬度
        BigDecimal latitude = httpRequestHeaderUtil.getLatitude();
        userAccountLocationService.save(
                new UserAccountLocation()
                        .setUserAccountId(selfId)
                        .setIp(ip)
                        .setAreaCodeByIp(byIpSelf.getAreaCode())
                        .setLongitude(longitude)
                        .setLatitude(latitude)
        );

    }


}
