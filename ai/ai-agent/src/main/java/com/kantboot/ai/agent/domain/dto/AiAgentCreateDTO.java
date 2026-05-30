package com.kantboot.ai.agent.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AiAgentCreateDTO {
    private String name;
    private String description;
    private String executorType;
    private String sshHost;
    private Integer sshPort;
    private String sshUser;
    private String sshPassword;
    private String sshWorkDir;
    private String localWorkDir;
    private Long modelId;
    private Long roleId;
    private String permissionMode;
    private List<String> allowList;
    private List<String> blockList;
    private Integer maxSteps;
}
