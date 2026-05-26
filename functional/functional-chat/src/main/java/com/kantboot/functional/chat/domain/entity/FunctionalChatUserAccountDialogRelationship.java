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
 * 聊天与用户的关系
 */
@Table(name="functional_chat_user_account_relationship")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatUserAccountDialogRelationship
    extends BaseEntity
    implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 会话ID
     */
    @Column(name = "dialog_id")
    private Long dialogId;

    /**
     * 未读数量
     */
    @Column(name = "unread_count")
    private Long unreadCount;

    /**
     * 编码
     */
    @Column(name = "code", unique = true,length = 128)
    private String code;

}
