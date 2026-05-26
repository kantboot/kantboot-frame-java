package com.kantboot.functional.email.domain.entity;

import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能短信实体类
 */
@Table(name="functional_email")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalEmail implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间（即发送时间）
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 短信内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 接收者
     */
    @Column(name = "t_to")
    private String to;

    /**
     * 消息类型
     * 对应 com.kantboot.functional.sms.enums.MessageTypeCodeEnum
     */
    @Column(name = "message_type_code")
    private String messageTypeCode;

    /**
     * 消息状态编码
     * 对应 com.kantboot.functional.sms.enums.MessageStatusCodeEnum
     */
    @Column(name = "message_status_code")
    private String messageStatusCode;

    /**
     * 发送失败原因编码
     */
    @Column(name = "message_status_fail_reason_code")
    private String messageStatusFailReasonCode;

    /**
     * 发送失败原因
     */
    @Column(name = "message_status_fail_reason",length = 3000)
    private String messageStatusFailReason;

    /**
     * 是否html
     */
    @Column(name = "is_html")
    private Boolean isHtml;

    /**
     * 主题
     */
    @Column(name = "subject",columnDefinition="TEXT")
    private String subject;


}
