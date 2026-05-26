package com.kantboot.thirdparty.baidu.translate.exception;

import com.kantboot.util.rest.exception.BaseException;

public class ThirdpartyBaiduTranslateException {

//  52001	请求超时	重试
//  52002	系统错误	重试
//  52003	未授权用户	请检查您的 appid 是否正确，或者服务是否开通
//  54000	必填参数为空	请检查是否少传参数
//  54001	签名错误	请检查您的签名生成方法
//  54003	访问频率受限	请降低您的调用频率
//  54004	账户余额不足	请前往管理控制台为账户充值
//  54009	语种检测失败	不在支持检测的语种范围内
//  58000	客户端IP非法	检查个人资料填写的IP地址是否正确，可前往管理控制平台修改，IP限制，IP可留空
//  58002	服务当前已关闭	请前往管理控制台开启服务

    public static BaseException CODE_52001 = BaseException.of(
            "thirdparty.baidu.translate.exception:52001",
            "请求超时，请重试",
            "zh_CN");

    public static BaseException CODE_52002 = BaseException.of(
            "thirdparty.baidu.translate.exception:52002",
            "系统错误，请重试",
            "zh_CN");

    public static BaseException CODE_52003 = BaseException.of(
            "thirdparty.baidu.translate.exception:52003",
            "未授权用户，请检查您的 appid 是否正确，或者服务是否开通",
            "zh_CN");

    public static BaseException CODE_54000 = BaseException.of(
            "thirdparty.baidu.translate.exception:54000",
            "必填参数为空，请检查是否少传参数",
            "zh_CN");

    public static BaseException CODE_54001 = BaseException.of(
            "thirdparty.baidu.translate.exception:54001",
            "签名错误，请检查您的签名生成方法",
            "zh_CN");

    public static BaseException CODE_54003 = BaseException.of(
            "thirdparty.baidu.translate.exception:54003",
            "访问频率受限，请降低您的调用频率",
            "zh_CN");

    public static BaseException CODE_54004 = BaseException.of(
            "thirdparty.baidu.translate.exception:54004",
            "账户余额不足，请前往管理控制台为账户充值",
            "zh_CN");

    public static BaseException CODE_54009 = BaseException.of(
            "thirdparty.baidu.translate.exception:54009",
            "语种检测失败，不在支持检测的语种范围内",
            "zh_CN");

    public static BaseException CODE_58000 = BaseException.of(
            "thirdparty.baidu.translate.exception:58000",
            "客户端IP非法，检查个人资料填写的IP地址是否正确，可前往管理控制平台修改，IP限制，IP可留空",
            "zh_CN");

    public static BaseException CODE_58002 = BaseException.of(
            "thirdparty.baidu.translate.exception:58002",
            "服务当前已关闭，请前往管理控制台开启服务",
            "zh_CN");

    public static BaseException exceptionOf(String code){
        return switch (code) {
            case "52001" -> CODE_52001;
            case "52002" -> CODE_52002;
            case "52003" -> CODE_52003;
            case "54000" -> CODE_54000;
            case "54001" -> CODE_54001;
            case "54003" -> CODE_54003;
            case "54004" -> CODE_54004;
            case "54009" -> CODE_54009;
            case "58000" -> CODE_58000;
            case "58002" -> CODE_58002;
            default -> BaseException.of(
                    "thirdparty.baidu.translate.exception:unknown",
                    "未知错误",
                    "zh_CN");
        };
    }


}
