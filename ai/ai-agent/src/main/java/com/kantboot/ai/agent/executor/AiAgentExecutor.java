package com.kantboot.ai.agent.executor;

public interface AiAgentExecutor extends AutoCloseable {
    String execute(String command) throws Exception;
}
