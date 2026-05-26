package com.kantboot.functional.file.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
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
 * 文件组管理的实体类
 * 用于管理文件组的上传、下载、路径等
 * Entity class for file group management
 * Used to manage file group upload, download, path, etc.
 *
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "functional_file_group")
@DynamicUpdate
@DynamicInsert
public class FunctionalFileGroup
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 文件组编码，用于文件组下载、删除、访问等
     * 如：头像、文章图片等
     * File group code, used for file group download, delete, access, etc.
     * Such as: avatar, article picture, etc.
     */
    @Column(name = "code", length = 64,unique = true)
    private String code;

    /**
     * 文件组名称
     * File group name
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件组描述
     * File group description
     */
    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    /**
     * 文件组路径
     * File group path
     */
    @Column(name = "path", columnDefinition="TEXT")
    private String path;

    /**
     * 缩略图路径
     * Thumbnail path
     */
    @Column(name = "thumbnail_path", columnDefinition="TEXT")
    private String thumbnailPath;

    /**
     * 是否有缩略图
     * Whether there is a thumbnail
     */
    @Column(name = "has_thumbnail")
    private Boolean hasThumbnail = false;

    /**
     * 是否压缩
     */
    @Column(name = "need_compress")
    private Boolean needCompress = false;

    /**
     * 压缩大小 单位B
     */
    @Column(name = "compress_size")
    private Long compressSize;

}
