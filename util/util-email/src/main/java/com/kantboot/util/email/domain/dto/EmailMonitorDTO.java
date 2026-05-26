package com.kantboot.util.email.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailMonitorDTO
    implements Serializable {

    /**
     * 端口
     */
    private Integer port = 993;

    private Boolean sslEnable = true;

}
