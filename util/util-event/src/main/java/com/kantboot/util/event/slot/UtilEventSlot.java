package com.kantboot.util.event.slot;

import com.kantboot.util.event.domain.dto.EventOnEndDTO;
import com.kantboot.util.event.domain.dto.EventOnInProgressDTO;
import com.kantboot.util.event.domain.dto.EventOnStartDTO;
import org.springframework.stereotype.Component;

@Component
public class UtilEventSlot {

    /**
     * 开始调用
     */
    public void onStart(EventOnStartDTO eventOnDTO){

    }

    /**
     * 结束调用
     */
    public void onEnd(EventOnEndDTO eventOnDTO){

    }

    /**
     * 进行中调用
     */
    public void onInProgress(EventOnInProgressDTO eventOnDTO){
    }

}