package com.kantboot.ai.chat.slot;

import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.domain.vo.AiChatMessageAllVO;
import com.kantboot.ai.chat.method.AiChatMethod;
import com.kantboot.util.rest.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiChatSlot {
    public void sendMessageHasStream(
            List<AiChatMessageAllVO> messages,
            AiChatModel model,
            AiChatMethod method
    ) {
        throw BaseException.of("noPluginImplementsAiChat", "No plugin implements AI chat");
    }
}
