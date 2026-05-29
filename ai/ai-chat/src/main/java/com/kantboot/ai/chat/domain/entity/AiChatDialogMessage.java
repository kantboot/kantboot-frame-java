package com.kantboot.ai.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Table(name = "ai_chat_dialog_message")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AiChatDialogMessage extends BaseEntity implements Serializable {

    @Column(name = "dialog_id")
    private Long dialogId;

    @Column(name = "role")
    private String role;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status_code", length = 20)
    private String statusCode;

    @Column(name = "user_account_id")
    private Long userAccountId;
}
