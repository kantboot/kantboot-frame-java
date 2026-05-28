package com.kantboot.developer.timer.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.log.domain.LoggerItem;
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
@Table(name = "developer_timer_logger_item")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class DeveloperTimerLoggerItem
    extends BaseEntity
    implements Serializable {

    @Column(name="timer_uuid")
    private String timerUuid;

    @Type(JsonBinaryType.class)
    @Column(name = "logger_items", columnDefinition = "JSON")
    private LoggerItem loggerItem;

    @Column(name = "nano_time")
    private Long nanoTime;

}
