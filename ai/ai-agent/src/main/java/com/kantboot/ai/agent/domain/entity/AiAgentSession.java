package com.kantboot.ai.agent.domain.entity;

import com.kantboot.ai.agent.constants.AiAgentSessionStatusConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name = "ai_agent_session")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AiAgentSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "user_account_id")
    private Long userAccountId;

    @Column(name = "task", columnDefinition = "TEXT")
    private String task;

    @Column(name = "status")
    private String status = AiAgentSessionStatusConstants.RUNNING;

    @Column(name = "total_steps")
    private Integer totalSteps = 0;
}
