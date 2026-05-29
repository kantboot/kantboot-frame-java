package com.kantboot.ai.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Table(name = "ai_chat_dialog")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AiChatDialog extends BaseEntity implements Serializable {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "user_account_id")
    private Long userAccountId;

    @Column(name = "language_code", length = 20)
    private String languageCode;
}
