package com.kantboot.functional.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 会话消息
 */
@Table(name="functional_chat_dialog_message")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatDialogMessage
        extends BaseEntity
        implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 接收者的用户ID（1对1 用户私聊时有效）
     */
    @Column(name = "user_account_id_of_receiver")
    private Long userAccountIdOfReceiver;

    /**
     * 用于查询的文字
     */
    @Lob
    @Column(name = "search_text",length = 65535)
    private String searchText;

    /**
     * 虚拟ID
     */
    @Column(name = "virtual_id",length = 65535)
    private String virtualId;

    /**
     * 其他类型
     */
    @Column(name = "other_type")
    private String otherType;

    /**
     * 其它识别号
     */
    @Column(name = "other_id")
    private String otherId;

    /**
     * 会话ID
     */
    @Column(name = "dialog_id")
    private Long dialogId;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @Column(name = "text_content",columnDefinition = "TEXT")
    private String textContent;

    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView;


}
