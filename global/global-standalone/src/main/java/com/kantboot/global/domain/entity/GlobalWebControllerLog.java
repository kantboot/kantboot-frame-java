package com.kantboot.global.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "global_web_controller_log")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class GlobalWebControllerLog
        extends BaseEntity
        implements Serializable {

    /**
     * URI
     */
    @Column(name = "request_uri", columnDefinition = "TEXT")
    private String requestUri;

    /**
     * 方法
     */
    @Column(name = "request_method", length = 16)
    private String requestMethod;

    /**
     * Query请求参数
     */
    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    /**
     * Body请求参数
     */
    @Type(JsonBinaryType.class)
    @Column(name = "requestBody", columnDefinition = "TEXT")
    private String requestBody;

    /**
     * 请求的content-type
     */
    @Column(name = "request_content_type", length = 128)
    private String requestContentType;

    /**
     * 请求的语言编码
     */
    @Column(name = "request_language_code", length = 128)
    private String requestLanguageCode;

    /**
     * 请求的客户端类型
     */
    @Column(name = "request_user_agent", length = 512)
    private String requestUserAgent;

    /**
     * 请求的客户端ip
     */
    @Column(name = "request_ip", length = 64)
    private String requestIp;

    /**
     * 请求的token
     */
    @Column(name = "request_token", length = 512)
    private String requestToken;

    /**
     * 请求的cookie
     */
    @Column(name = "request_cookie", columnDefinition = "TEXT")
    private String requestCookie;

    /**
     * 响应状态码
     */
    @Column(name = "response_status_code")
    private Integer responseStatusCode;

    /**
     * 客户端浏览器
     */
    @Column(name = "request_browser", length = 128)
    private String requestBrowser;

    /**
     * 客户端浏览器版本
     */
    @Column(name = "request_browser_version", length = 128)
    private String requestBrowserVersion;

    /**
     * 客户端操作系统
     */
    @Column(name = "request_os", length = 128)
    private String requestOs;

    /**
     * 客户端操作系统版本
     */
    @Column(name = "request_os_version", length = 128)
    private String requestOsVersion;


    /**
     * 请求的用户ID
     */
    @Column(name = "user_account_id_of_request")
    private Long userAccountIdOfRequest;

    /**
     * 是否登录
     */
    @Column(name = "is_login")
    private Boolean isLogin;

    /**
     * 是否被拦截
     */
    @Column(name = "is_intercepted")
    private Boolean isIntercepted;

    /**
     * 拦截类型
     */
    @Column(name = "intercept_type", length = 128)
    private String interceptType;

    /**
     * 响应结果
     */
    @JSONField(serialize = false)
    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    /**
     * 区域编码
     */
    @Column(name = "area_code", length = 64)
    private String areaCode;

}
