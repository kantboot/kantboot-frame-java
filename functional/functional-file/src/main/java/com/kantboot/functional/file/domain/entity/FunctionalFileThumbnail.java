package com.kantboot.functional.file.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
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

/**
 * 文件缩略图实体类
 * 用于存储文件的缩略图信息
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "functional_file_thumbnail")
@DynamicUpdate
@DynamicInsert
public class FunctionalFileThumbnail
    extends BaseEntity
    implements Serializable {

    /**
     * 文件id
     */
    @Column(name = "file_id")
    private Long fileId;

    /**
     * quality 缩略质量
     */
    @Column(name = "quality")
    private Float quality;

    /**
     * 文件组编码
     * 如：头像、文章图片等
     */
    @Column(name = "group_code",length = 64)
    private String groupCode;

    /**
     * 编码
     */
    @Column(name = "code",length = 64)
    private String code;

    /**
     * 上传时的文件名
     */
    @Column(name = "original_name")
    private String originalName;

    /**
     * 文件名
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 文件ContentType
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 文件大小
     */
    @Column(name = "size")
    private Long size;

    /**
     * 文件MD5值，通过MD5值判断文件是否重复
     */
    @Column(name = "md5",length = 64)
    private String md5;

    /**
     * 文件上传者id
     */
    @Column(name = "user_account_id_of_upload")
    private Long userIdAccountOfUpload;

    /**
     * 上传时IP
     */
    @Column(name = "ip_of_upload")
    private String ipOfUpload;

}
