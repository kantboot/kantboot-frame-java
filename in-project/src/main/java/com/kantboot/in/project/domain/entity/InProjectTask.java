package com.kantboot.in.project.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "in_project_task")
@DynamicUpdate
@DynamicInsert
public class InProjectTask
    extends BaseEntity
    implements Serializable {

    /**
     * 发布者ID
     */
    @Column(name = "user_account_od_publisher_id")
    private Long userAccountOdPublisherId;

    /**
     * 任务标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 格式
     */
    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView;

    /**
     * 任务类型
     */
    @Column(name = "typeCode")
    private String typeCode;

    /**
     * 指定完成时间
     */
    @Column(name = "gmt_deadline")
    private java.util.Date gmtDeadline;

    /**
     * 指定受理人
     */
    @Column(name = "user_account_id_of_assignee")
    private Long userAccountIdOfAssignee;

    /**
     * 已完成
     */
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

}
