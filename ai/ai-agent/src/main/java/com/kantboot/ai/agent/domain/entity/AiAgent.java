package com.kantboot.ai.agent.domain.entity;

import com.kantboot.ai.agent.constants.AiAgentExecutorTypeConstants;
import com.kantboot.ai.agent.constants.AiAgentPermissionModeConstants;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "ai_agent")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AiAgent implements Serializable {

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

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** LOCAL 或 SSH */
    @Column(name = "executor_type")
    private String executorType = AiAgentExecutorTypeConstants.LOCAL;

    @Column(name = "ssh_host")
    private String sshHost;

    @Column(name = "ssh_port")
    private Integer sshPort = 22;

    @Column(name = "ssh_user")
    private String sshUser;

    @Column(name = "ssh_password")
    private String sshPassword;

    @Column(name = "ssh_work_dir")
    private String sshWorkDir;

    @Column(name = "local_work_dir")
    private String localWorkDir;

    /** 关联 AiChatModel.id */
    @Column(name = "model_id")
    private Long modelId;

    /** 关联 AiChatRole.id，可选，用于提供 system 预设 */
    @Column(name = "role_id")
    private Long roleId;

    /** AUTO / WHITELIST / BLACKLIST / ASK / PLAN */
    @Column(name = "permission_mode")
    private String permissionMode = AiAgentPermissionModeConstants.AUTO;

    @Type(JsonBinaryType.class)
    @Column(name = "allow_list", columnDefinition = "jsonb")
    private List<String> allowList;

    @Type(JsonBinaryType.class)
    @Column(name = "block_list", columnDefinition = "jsonb")
    private List<String> blockList;

    @Column(name = "max_steps")
    private Integer maxSteps = 120;
}
