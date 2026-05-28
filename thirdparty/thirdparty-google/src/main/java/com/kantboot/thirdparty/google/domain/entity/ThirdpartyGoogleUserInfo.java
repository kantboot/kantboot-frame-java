package com.kantboot.thirdparty.google.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "thirdparty_google_user_info")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey
public class ThirdpartyGoogleUserInfo
    extends BaseEntity
    implements Serializable {

    @Column(name = "user_account_id")
    private Long userAccountId;

    @Column(name = "google_id",unique = true)
    private String googleId;

    @Column(name = "email")
    private String email;

    @Column(name = "verified_email")
    private Boolean verifiedEmail;

    @Column(name = "name")
    private String name;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "picture")
    private String picture;


}
