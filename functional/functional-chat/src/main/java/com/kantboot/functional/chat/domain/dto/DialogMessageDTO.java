package com.kantboot.functional.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DialogMessageDTO
    implements Serializable {

    private Long dialogId;

    private Object ktFormatOfView;

    private String textContent;

    /**
     * 虚拟ID
     */
    private String virtualId;

}
