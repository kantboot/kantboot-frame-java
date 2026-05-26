package com.kantboot.util.email.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件发送数据传输对象
 * 用于封装邮件发送所需的参数
 * 包括接收者、主题、内容和是否为HTML格式
 */
@Data
public class EmailSendDTO
        implements Serializable {


    /**
     * 端口
     */
    private Integer port;

    /**
     * 接收者
     */
    private String to;

    /**
     * 主题
     */
    private String subject;

    /**
     * 邮箱内容
     */
    private String content;

    /**
     * 是否为html
     */
    private Boolean isHtml;


}
