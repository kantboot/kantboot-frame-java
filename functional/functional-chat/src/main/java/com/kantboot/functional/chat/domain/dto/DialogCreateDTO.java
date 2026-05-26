package com.kantboot.functional.chat.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class DialogCreateDTO implements Serializable {

    /**
     * 会话名称
     */
    private String name;

    /**
     * 会话类型编码
     * 私聊：oneToOne
     * 群聊：group
     * 客服：customerService
     */
    private String type;

    /**
     * 会话成员
     * 创建群聊时需要
     */
    private List<Long> userAccountIds;

}
