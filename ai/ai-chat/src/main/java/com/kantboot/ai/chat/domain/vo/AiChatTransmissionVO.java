package com.kantboot.ai.chat.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AiChatTransmissionVO implements Serializable {
    private Long dialogId;
    private Long messageId;
    private String role;
    private String text;
    private String content;
    private Boolean done;
}
