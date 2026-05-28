package com.kantboot.init;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class KantbootInit {

    @Value("${kantboot.init:true}")
    private boolean init;

}
