package com.kantboot.developer.event.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.log.domain.LoggerItem;
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

@Entity
@Getter
@Setter
@Table(name = "developer_event_logger_item")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class DeveloperEventLoggerItem
    extends BaseEntity
    implements Serializable {

    @Column(name="event_uuid")
    private String eventUuid;

    @Type(JsonBinaryType.class)
    @Column(name = "logger_items", columnDefinition = "JSON")
    private LoggerItem loggerItem;

    @Column(name = "nano_time")
    private Long nanoTime;

}
