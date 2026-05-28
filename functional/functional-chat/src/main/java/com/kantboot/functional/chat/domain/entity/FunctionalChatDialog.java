package com.kantboot.functional.chat.domain.entity;

import com.kantboot.user.account.domain.entity.UserAccount;
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
@Table(name="functional_chat_dialog")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalChatDialog
    extends BaseEntity
    implements Serializable {

    /**
     * 会话名称
     */
    @Column(name = "name",columnDefinition = "TEXT")
    private String name;

    /**
     * 会话类型编码
     * 私聊：oneToOne
     * 群聊：group
     */
    @Column(name = "type")
    private String type;

    /**
     * 私聊的识别号
     */
    @Column(name = "one_to_one_identifier",unique = true)
    private String oneToOneIdentifier;

    /**
     * 用户账号1
     */
    @Column(name = "user_account_id_1_of_one_to_one")
    private Long userAccountId1OfOneToOne;

    /**
     * 用户账号2
     */
    @Column(name = "user_account_id_2_of_one_to_one")
    private Long userAccountId2OfOneToOne;

    /**
     * 条数
     */
    @Column(name = "message_count")
    private Long messageCount = 0L;

    /**
     * 最后一条消息
     */
    @Column(name = "last_message_json_str",columnDefinition = "TEXT")
    private String lastMessageJsonStr;

    /**
     * 最后一条消息
     */
    @Column(name = "last_text_message",columnDefinition = "TEXT")
    private String lastTextMessage;

    /**
     * 最后一条消息ID
     */
    @Column(name = "last_message_id")
    private Long lastMessageId;

    @OneToOne
    @JoinColumn(name = "user_account_id_1_of_one_to_one",insertable = false,updatable = false)
    private UserAccount userAccount1;

    @OneToOne
    @JoinColumn(name = "user_account_id_2_of_one_to_one",insertable = false,updatable = false)
    private UserAccount userAccount2;

    /**
     * 未读数量
     */
    @Transient
    private Long unreadCount = 0L;

}
