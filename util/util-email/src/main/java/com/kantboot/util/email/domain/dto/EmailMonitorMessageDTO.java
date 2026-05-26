package com.kantboot.util.email.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailMonitorMessageDTO
    implements Serializable {

    private String subject;

    private String from;

    private String sentDate;

}
