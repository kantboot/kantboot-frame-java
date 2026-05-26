package com.kantboot.util.timer.service.impl;

import com.kantboot.util.timer.domain.dto.TimerDTO;
import com.kantboot.util.timer.init.TimerInit;
import com.kantboot.util.timer.manager.TimerManager;
import com.kantboot.util.timer.service.ITimerService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimerServiceImpl implements ITimerService {

    @Resource
    private TimerManager timerManager;

    @Override
    public List<TimerDTO> getAll() {
        return TimerInit.TIMER_LIST;
    }

    @Override
    public TimerDTO getByCode(String code) {
        return TimerInit.getTimer(code);
    }

    @Override
    public void startTimer(String code) {
        timerManager.startTimer(code);
    }

    @Override
    public void stopTimer(String code) {
        timerManager.stopTimer(code);
    }

    @Override
    public void restartTimer(String code) {
        timerManager.restartTimer(code);
    }

    @Override
    public void startAllTimers() {
        timerManager.startAllTimers();
    }

    @Override
    public void stopAllTimers() {
        timerManager.stopAllTimers();
    }

    @Override
    public boolean isTimerRunning(String code) {
        return timerManager.isTimerRunning(code);
    }
}
