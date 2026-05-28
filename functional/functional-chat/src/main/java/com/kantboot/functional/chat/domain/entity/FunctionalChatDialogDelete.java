package com.kantboot.functional.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 会话
 */
@Table(name="functional_chat_dialog_delete")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatDialogDelete
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
     * 编码
     */
    @Column(name = "code", unique = true,length = 128)
    private String code;

    /**
     * 最后一条消息ID
     */
    @Column(name = "last_message_id")
    private Long lastMessageId;


    /**
     * 是否已释放
     */
    @Column(name = "is_released")
    private Boolean isReleased = false;

}
