package com.kantboot.ai.agent.slot;

import com.kantboot.ai.agent.domain.entity.AiAgent;
import com.kantboot.ai.agent.executor.AiAgentExecutor;
import com.kantboot.util.rest.exception.BaseException;
import org.springframework.stereotype.Service;

@Service
public class AiAgentExecutorSlot {

    public AiAgentExecutor create(AiAgent agent) {
        throw BaseException.of("noPluginImplementsAgentExecutor", "No plugin implements agent executor");
    }
}
