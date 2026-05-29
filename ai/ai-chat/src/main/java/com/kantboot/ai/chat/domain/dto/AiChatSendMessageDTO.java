package com.kantboot.ai.chat.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AiChatSendMessageDTO implements Serializable {
    private Long dialogId;
    private String content;
}
