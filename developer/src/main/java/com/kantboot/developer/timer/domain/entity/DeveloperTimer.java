package com.kantboot.developer.timer.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "developer_timer")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class DeveloperTimer
    extends BaseEntity
    implements Serializable {

    @Column(name="uuid",unique = true, nullable = false, length = 64)
    private String uuid;

    /**
     * 方法加参数
     */
    @Column(name = "method_with_params", columnDefinition = "TEXT")
    private String methodWithParams;

    @Column(name = "code", nullable = false, length = 64)
    private String code;

    @Column(name = "data", columnDefinition = "TEXT")
    private Date gmtOnStart;

    @Column(name = "gmt_on_end", columnDefinition = "TEXT")
    private Date gmtOnEnd;

    @Type(JsonBinaryType.class)
    @Column(name = "t_data", columnDefinition = "JSON")
    private List<Object> data;

    @OrderBy("nanoTime asc")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "timer_uuid", referencedColumnName = "uuid")
    private List<DeveloperTimerLoggerItem> developerTimerLoggerItems;

    @Column(name = "is_exception_end")
    private Boolean isExceptionEnd;

    @Type(JsonBinaryType.class)
    @Column(name = "t_exception", columnDefinition = "JSON")
    private Object exception;

}
