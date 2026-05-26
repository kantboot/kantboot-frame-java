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
 * 被用户账号删除的对话
 */
@Table(name="functional_chat_dialog_user_account_deleted")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatDialogUserAccountDeleted
        extends BaseEntity
        implements Serializable {

    /**
     * 对话ID
     */
    @Column(name = "dialog_id")
    private Long dialogId;

    /**
     * 被删除的用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 是否对话被删除
     */
    @Column(name = "is_dialog_deleted")
    private Boolean isDialogDeleted;

    /**
     * 删除前的最后ID
     */
    @Column(name = "last_message_id")
    private Long lastMessageId;

}
