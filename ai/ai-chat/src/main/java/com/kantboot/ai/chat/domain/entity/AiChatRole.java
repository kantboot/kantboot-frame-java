package com.kantboot.ai.chat.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "ai_chat_role")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AiChatRole extends BaseEntity implements Serializable {

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "category_code", length = 64)
    private String categoryCode;

    @Column(name = "gender_code", length = 20)
    private String genderCode;

    @Column(name = "gmt_birthday")
    private Date gmtBirthday;

    @Column(name = "age")
    private Integer age;

    @Column(name = "file_id_of_avatar")
    private Long fileIdOfAvatar;

    @Column(name = "source_language_code", length = 20)
    private String sourceLanguageCode;

    @Column(name = "priority")
    private Integer priority;

    @OneToMany
    @JoinColumn(name = "role_id")
    @OrderBy("priority DESC")
    private List<AiChatRoleLabel> labels;

    @OneToMany
    @JoinColumn(name = "role_id")
    private List<AiChatRoleLanguageSupport> languageSupports;
}
