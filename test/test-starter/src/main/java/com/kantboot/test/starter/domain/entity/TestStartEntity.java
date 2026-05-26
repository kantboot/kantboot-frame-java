package com.kantboot.test.starter.domain.entity;

import com.kantboot.util.i18n.annotation.I18nBottomKey;
import com.kantboot.util.i18n.annotation.I18nCenterKey;
import com.kantboot.util.i18n.annotation.I18nSave;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Table(name = "test_start_entity")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey(key = "TestStartEntity")
public class TestStartEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @I18nCenterKey
    private Long id;

    @I18nBottomKey(key = "name")
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @I18nBottomKey(key = "description")
    @Column(name = "description", length = 256)
    private String description;

    @I18nSave
    @Column(name = "i18n_translations",columnDefinition = "TEXT")
    private String i18nTranslations;

}
