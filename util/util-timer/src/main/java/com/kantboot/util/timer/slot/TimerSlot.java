package com.kantboot.util.timer.slot;

import com.kantboot.util.timer.domain.dto.TimerOnEndDTO;
import com.kantboot.util.timer.domain.dto.TimerOnInProgressDTO;
import com.kantboot.util.timer.domain.dto.TimerOnStartDTO;
import org.springframework.stereotype.Component;

@Component
public class TimerSlot {

    /**
     * 开始调用
     */
    public void onStart(TimerOnStartDTO onDTO){

    }

    /**
     * 进行中调用
     */
    public void onInProgress(TimerOnInProgressDTO onDTO){

    }

    /**
     * 结束调用
     */
    public void onEnd(TimerOnEndDTO onDTO){

    }



}
