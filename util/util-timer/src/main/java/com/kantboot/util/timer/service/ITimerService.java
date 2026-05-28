package com.kantboot.util.timer.service;

import com.kantboot.util.timer.domain.dto.TimerDTO;

import java.util.List;

/**
 * 定时器服务接口
 *
 * @author 方某方
 */
public interface ITimerService {

    /**
     * 查询所有定时器
     */
    List<TimerDTO> getAll();

    /**
     * 根据编码获取定时器
     */
    TimerDTO getByCode(String code);

    /**
     * 启动定时器
     */
    void startTimer(String code);

    /**
     * 停止定时器
     */
    void stopTimer(String code);

    /**
     * 重启定时器
     */
    void restartTimer(String code);

    /**
     * 启动所有定时器
     */
    void startAllTimers();

    /**
     * 停止所有定时器
     */
    void stopAllTimers();

    /**
     * 检查定时器是否运行
     */
    boolean isTimerRunning(String code);
}
