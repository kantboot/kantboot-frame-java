package com.kantboot.ai.agent.exception;

import com.kantboot.util.rest.exception.BaseException;

public class AiAgentException {
    public static final BaseException AGENT_NOT_EXIST = BaseException.of("aiAgentNotExist", "Agent does not exist");
    public static final BaseException SESSION_NOT_EXIST = BaseException.of("aiAgentSessionNotExist", "Agent session does not exist");
    public static final BaseException MODEL_NOT_EXIST = BaseException.of("aiAgentModelNotExist", "AI model does not exist");
    public static final BaseException COMMAND_BLOCKED = BaseException.of("aiAgentCommandBlocked", "Command is blocked by permission rules");
    public static final BaseException SESSION_LOCKED = BaseException.of("aiAgentSessionLocked", "Agent session is already running");
    public static final BaseException ASK_MODE_SSE_NOT_SUPPORTED = BaseException.of("aiAgentAskModeSseNotSupported", "ASK mode requires WebSocket connection");
}
