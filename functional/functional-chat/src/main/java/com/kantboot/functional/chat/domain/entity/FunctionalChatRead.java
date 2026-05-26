package com.kantboot.functional.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 会话已读
 */
@Table(name="functional_chat_read")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatRead
    extends BaseEntity
    implements Serializable {

    /**
     * 对应的用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 会话ID
     */
    @Column(name = "dialog_id")
    private Long dialogId;

    /**
     * 消息ID
     */
    @Column(name = "message_id")
    private Long messageId;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Boolean read = false;
}
