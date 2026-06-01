package com.kantboot.official.plugin.functional.agent.plugin;

import com.kantboot.ai.agent.constants.AiAgentExecutorTypeConstants;
import com.kantboot.ai.agent.domain.entity.AiAgent;
import com.kantboot.ai.agent.executor.AiAgentExecutor;
import com.kantboot.ai.agent.slot.AiAgentExecutorSlot;
import com.kantboot.official.plugin.functional.agent.executor.LocalAgentExecutor;
import com.kantboot.official.plugin.functional.agent.executor.SshAgentExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfficialPluginFunctionalAgentPlugin {

    @Bean
    public AiAgentExecutorSlot aiAgentExecutorSlot() {
        return new AiAgentExecutorSlot() {
            @Override
            public AiAgentExecutor create(AiAgent agent) {
                String type = agent.getExecutorType();
                if (AiAgentExecutorTypeConstants.SSH.equals(type)) {
                    int port = agent.getSshPort() != null ? agent.getSshPort() : 22;
                    try {
                        return new SshAgentExecutor(
                                agent.getSshHost(),
                                port,
                                agent.getSshUser(),
                                agent.getSshPassword(),
                                agent.getSshWorkDir()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("SSH connection failed: " + e.getMessage(), e);
                    }
                }
                return new LocalAgentExecutor(agent.getLocalWorkDir());
            }
        };
    }
}
