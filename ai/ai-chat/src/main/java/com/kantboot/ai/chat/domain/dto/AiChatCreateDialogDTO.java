package com.kantboot.ai.chat.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AiChatCreateDialogDTO implements Serializable {
    private Long roleId;
    private Long modelId;
    private String languageCode;
}
