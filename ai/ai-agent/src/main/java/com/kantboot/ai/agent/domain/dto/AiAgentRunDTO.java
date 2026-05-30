package com.kantboot.ai.agent.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AiAgentRunDTO {
    private Long agentId;
    private String task;
}
