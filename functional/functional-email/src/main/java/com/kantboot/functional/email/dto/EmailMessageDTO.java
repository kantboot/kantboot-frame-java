package com.kantboot.functional.email.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class EmailMessageDTO implements Serializable {

    private String email;
    private String subject;
    private String content;
    private Boolean isHtml;

}
