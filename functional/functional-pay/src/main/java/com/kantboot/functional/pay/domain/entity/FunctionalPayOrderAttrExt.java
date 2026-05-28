package com.kantboot.functional.pay.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Getter
@Setter
@MappedSuperclass
public class FunctionalPayOrderAttrExt
    implements Serializable {
}
