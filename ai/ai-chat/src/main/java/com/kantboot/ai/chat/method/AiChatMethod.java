package com.kantboot.ai.chat.method;

public abstract class AiChatMethod {
    public abstract void run(String text, String content, Boolean done);
    public abstract void finish(String content);
}
