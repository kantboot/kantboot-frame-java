package com.kantboot.ai.agent.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AiAgentStepVO {
    private Long sessionId;
    private Long stepId;
    private Integer stepIndex;
    private String thought;
    private String command;
    private String output;
    private String status;
    private Boolean done;
    private String errorMessage;
}
