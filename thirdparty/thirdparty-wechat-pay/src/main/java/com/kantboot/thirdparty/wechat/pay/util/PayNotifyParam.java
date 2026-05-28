package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 微信支付的通知类
 * 用于接收微信支付的通知
 * @author 方某方
 */
@Slf4j
@Data
public class PayNotifyParam implements Serializable {

    /**
     * 返回状态码
     * 错误码，SUCCESS为清算机构接收成功，其他错误码为失败。
     * 示例值：FAIL
     */
    private String code;

    /**
     * 返回信息
     * 返回信息，如非空，为错误原因。
     * 示例值：失败
     */
    private String message;

    /**
     * 通知ID
     * 通知的唯一ID
     * 示例值：EV-2018022511223320873
     */
    private String id;

    /**
     * 通知创建时间
     * 通知创建的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，
     * yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，
     * HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。
     * 例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。
     * 示例值：2015-05-20T13:29:35+08:00
     */
    @JSONField(name="create_time")
    private String createTime;

    /**
     * 通知类型
     * 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS
     * 示例值：TRANSACTION.SUCCESS
     */
    @JSONField(name="resource_type")
    private String resourceType;

    /**
     * 通知数据类型
     * 通知的资源数据类型，支付成功通知为encrypt-resource
     * 示例值：encrypt-resource
     */
    @JSONField(name="event_type")
    private String eventType;

    /**
     * 	回调摘要
     * 示例值：支付成功
     */
    private String summary;

    /**
     * 通知资源数据
     * json格式，见示例
     */
    private Resource resource;


    @Data
    public static class Resource{

        /**
         * 原始回调类型，为transaction
         * 示例值：transaction
         */
        @JSONField(name="original_type")
        private String originalType;

        /**
         * 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
         * 示例值：AEAD_AES_256_GCM
         */
        private String algorithm;

        /**
         * Base64编码后的开启/停用结果数据密文
         * 示例值：sadsadsadsad
         */
        private String ciphertext;

        /**
         * 附加数据
         * 示例值：fdasfwqewlkja484w
         */
        @JSONField(name="associated_data")
        private String associatedData;

        /**
         * 加密使用的随机串
         * 示例值：fdasflkja484w
         */
        private String nonce;

    }


    /**
     * 参数解密
     */
    public String decrypt(String mchKey) {
        String encryptedData = this.getResource().getCiphertext();
        String encode = "UTF-8";
        String nonce = this.getResource().getNonce();
        String associatedData = this.getResource().getAssociatedData();

        log.info("key:{}，\ndecryptData:{}，\nencode:{}，\nnonce:{}，\nassociatedData:{}",mchKey,encryptedData,encode,nonce,associatedData);

        return WeChatNotificationDecryptor.decryptNotification(encryptedData,mchKey,nonce,associatedData);
    }





}
