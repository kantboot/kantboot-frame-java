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
 * 文件管理实体类
 * 用于管理文件的上传、下载等
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "functional_file")
@DynamicUpdate
@DynamicInsert
public class FunctionalFile
    extends BaseEntity
    implements Serializable {

    /**
     * 文件组编码
     * 如：头像、文章图片等
     * File group code
     * Such as: avatar, article picture, etc.
     */
    @Column(name = "group_code",length = 64)
    private String groupCode;

    /**
     * 编码
     * 用于文件下载、删除、访问等（以上操作，也可以通过文件ID进行）
     * Code
     * Used for file download, delete, access, etc. (The above operations can also be performed through the file ID)
     */
    @Column(name = "code",length = 64)
    private String code;

    /**
     * 全编码
     * 用于文件下载、删除、访问等（以上操作，也可以通过文件ID进行）
     * Full code
     * Used for file download, delete, access, etc. (The above operations can also be performed through the file ID)
     */
    @Column(name = "full_code",length = 64)
    private String fullCode;

    /**
     * 上传时的文件名
     * Original file name when uploaded
     */
    @Column(name = "original_name")
    private String originalName;

    /**
     * 文件名
     * File name
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件路径
     * File path
     */
    @Column(name = "path")
    private String path;

    /**
     * 文件类型
     * 如：image/jpeg、application/pdf等
     * File type
     * Such as: image/jpeg, application/pdf, etc.
     */
    @Column(name = "type")
    private String type;

    /**
     * 文件的ContentType
     * 如：image/jpeg、application/pdf等
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 文件大小
     * File size
     */
    @Column(name = "size")
    private Long size;

    /**
     * 文件MD5值，通过MD5值判断文件是否重复
     * File MD5 value, determine whether the file is duplicated by MD5 value
     */
    @Column(name = "md5",length = 64)
    private String md5;

    /**
     * 文件上传者id
     * User ID of the file uploader
     */
    @Column(name = "user_account_id_of_upload")
    private Long userAccountIdOfUpload;

    /**
     * 上传时IP
     * IP when uploading
     */
    @Column(name = "ip_of_upload")
    private String ipOfUpload;

}
