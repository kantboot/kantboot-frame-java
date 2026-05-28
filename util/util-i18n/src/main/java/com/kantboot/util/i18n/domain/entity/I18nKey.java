package com.kantboot.util.i18n.domain.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class I18nKey implements Serializable {

    private String topKey;

    private String centerKey;

    private String bottomKey;

}
