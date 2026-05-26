package com.kantboot.util.timer.web.admin.controller;

import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.exception.BaseException;
import com.kantboot.util.rest.result.RestResult;
import com.kantboot.util.timer.domain.dto.TimerDTO;
import com.kantboot.util.timer.service.ITimerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定时器管理控制器
 *
 * @author 方某方
 */
@RestController
@RequestMapping("/util-timer-web/admin/timer")
@AuthInit(
        name = "定时器管理",
        description = "用于管理系统中的定时器，包括启动、停止、重启等操作",
        sourceLanguageCode = "zh_CN"
)
public class TimerControllerOfAdmin {

    @Resource
    private ITimerService timerService;

    /**
     * 获取所有定时器
     */
    @RequestMapping("/getAll")
    @AuthInit(
            name = "获取所有定时器",
            description = "获取系统中所有已配置的定时器列表",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<List<TimerDTO>> getAll() {
        return RestResult.success(timerService.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 根据编码获取定时器
     */
    @RequestMapping("/getByCode")
    @AuthInit(
            name = "根据编码获取定时器",
            description = "根据定时器的唯一编码获取定时器详情",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<TimerDTO> getByCode(@RequestParam("code") String code) {
        TimerDTO timer = timerService.getByCode(code);
        if (timer == null) {
            throw BaseException.of("timerNotFount:" + code, "定时器未找到: " + code, "zh_CN");
        }
        return RestResult.success(timer, CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 启动定时器
     */
    @RequestMapping("/start")
    @AuthInit(
            name = "启动定时器",
            description = "根据定时器编码启动指定的定时器",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Void> startTimer(@RequestParam("code") String code) {
        timerService.startTimer(code);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 停止定时器
     */
    @RequestMapping("/stop")
    @AuthInit(
            name = "停止定时器",
            description = "根据定时器编码停止指定的定时器",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Void> stopTimer(@RequestParam("code") String code) {
        timerService.stopTimer(code);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 重启定时器
     */
    @RequestMapping("/restart")
    @AuthInit(
            name = "重启定时器",
            description = "根据定时器编码重启指定的定时器",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Void> restartTimer(@RequestParam("code") String code) {
        timerService.restartTimer(code);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 启动所有定时器
     */
    @RequestMapping("/startAll")
    @AuthInit(
            name = "启动所有定时器",
            description = "启动系统中所有已配置的定时器",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Void> startAllTimers() {
        timerService.startAllTimers();
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 停止所有定时器
     */
    @RequestMapping("/stopAll")
    @AuthInit(
            name = "停止所有定时器",
            description = "停止系统中所有正在运行的定时器",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Void> stopAllTimers() {
        timerService.stopAllTimers();
        return RestResult.success(null,CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 检查定时器状态
     */
    @RequestMapping("/status")
    @AuthInit(
            name = "获取定时器状态",
            description = "检查指定定时器是否正在运行",
            sourceLanguageCode = "zh_CN"
    )
    public RestResult<Boolean> getTimerStatus(@RequestParam("code") String code) {
        boolean isRunning = timerService.isTimerRunning(code);
        return RestResult.success(isRunning, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }
}
