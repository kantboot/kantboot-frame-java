package com.kantboot.developer.timer.plugin;

import com.kantboot.developer.timer.dao.repository.DeveloperTimerLoggerItemRepository;
import com.kantboot.developer.timer.dao.repository.DeveloperTimerRepository;
import com.kantboot.developer.timer.domain.entity.DeveloperTimer;
import com.kantboot.developer.timer.domain.entity.DeveloperTimerLoggerItem;
import com.kantboot.util.timer.domain.dto.TimerOnEndDTO;
import com.kantboot.util.timer.domain.dto.TimerOnInProgressDTO;
import com.kantboot.util.timer.domain.dto.TimerOnStartDTO;
import com.kantboot.util.timer.slot.TimerSlot;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperPluginOfUtilTimerSlot {

    @Resource
    private DeveloperTimerRepository repository;

    @Resource
    private DeveloperTimerLoggerItemRepository loggerItemRepository;

    @Bean
    public TimerSlot timerSlot() {
        return new TimerSlot() {
            /**
             * 开始调用
             */
            @Override
            public void onStart(TimerOnStartDTO eventOnDTO){
                DeveloperTimer eventTimer = new DeveloperTimer();
                eventTimer.setUuid(eventOnDTO.getUuid());
                eventTimer.setCode(eventOnDTO.getCode());
                eventTimer.setGmtOnStart(eventOnDTO.getGmtOnStart());
                eventTimer.setData(eventOnDTO.getData());
                repository.save(eventTimer);
            }

            /**
             * 进行中调用
             */
            @SneakyThrows
            @Override
            public void onInProgress(TimerOnInProgressDTO eventOnInProgressDTO){
                DeveloperTimerLoggerItem item = new DeveloperTimerLoggerItem();
                item.setTimerUuid(eventOnInProgressDTO.getUuid());
                item.setLoggerItem(eventOnInProgressDTO.getLoggerItem());
                item.setNanoTime(eventOnInProgressDTO.getLoggerItem().getNanoTime());
                loggerItemRepository.save(item);
            }

            /**
             * 结束调用
             */
            @Override
            public void onEnd(TimerOnEndDTO eventOnDTO){
                DeveloperTimer event = repository.findByUuid(eventOnDTO.getUuid());
                if(event==null){
                    OnEndSave(eventOnDTO,0);
                    return;
                }
                event.setGmtOnEnd(eventOnDTO.getGmtOnEnd());
                event.setIsExceptionEnd(eventOnDTO.getIsExceptionEnd());
                event.setException(eventOnDTO.getException());
                repository.save(event);
            }

            @SneakyThrows
            private void OnEndSave(TimerOnEndDTO eventOnDTO, int i){
                DeveloperTimer event = repository.findByUuid(eventOnDTO.getUuid());
                if(event==null){
                    i++;
                    if(i>60){
                        return;
                    }
                    Thread.sleep(1000);
                    OnEndSave(eventOnDTO,i);
                    return;
                }
                event.setGmtOnEnd(eventOnDTO.getGmtOnEnd());
                event.setIsExceptionEnd(eventOnDTO.getIsExceptionEnd());
                event.setException(eventOnDTO.getException());
                repository.save(event);
            }


        };
    }

}
