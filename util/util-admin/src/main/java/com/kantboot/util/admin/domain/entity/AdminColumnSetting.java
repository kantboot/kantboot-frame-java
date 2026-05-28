package com.kantboot.util.admin.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminColumnSetting
        implements Serializable {

    /**
     * 在时间类型的列中，是时间显示的类型
     */
    private String format;

    /**
     * 操作编码 eq、like、vague、gt、lt、ge、le、openInterval、closeInterval、between
     */
    private String operatorCode;

    /**
     * 分组
     * 例如：文件分组的编码
     */
    private String groupCode;

}
