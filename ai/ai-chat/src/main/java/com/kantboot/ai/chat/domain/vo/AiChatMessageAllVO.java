package com.kantboot.ai.chat.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AiChatMessageAllVO implements Serializable {
    private String role;
    private String content;
}
