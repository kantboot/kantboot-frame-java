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
 * 会话成员
 * @author 方某方
 */
@Table(name="functional_chat_dialog_person")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatDialogPerson
        extends BaseEntity
        implements Serializable {

    /**
     * 用户账号
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 会话ID
     */
    @Column(name = "dialog_id")
    private Long dialogId;

    /**
     * 是否为群主
     */
    @Column(name = "is_owner")
    private Boolean isOwner;

    /**
     * 是否为管理者
     */
    @Column(name = "is_manager")
    private Boolean isManager;

    @Column(name = "other_type")
    private String otherType;

    /**
     * 其它识别号，如机器人的识别号，AI识别号等
     */
    @Column(name = "other_id")
    private String otherId;

}
