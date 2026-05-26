package com.kantboot.developer.event.plugin;

import com.alibaba.fastjson2.JSON;
import com.kantboot.developer.event.dao.repository.DeveloperEventLoggerItemRepository;
import com.kantboot.developer.event.dao.repository.DeveloperEventRepository;
import com.kantboot.developer.event.domain.entity.DeveloperEvent;
import com.kantboot.developer.event.domain.entity.DeveloperEventLoggerItem;
import com.kantboot.util.event.domain.dto.EventOnEndDTO;
import com.kantboot.util.event.domain.dto.EventOnInProgressDTO;
import com.kantboot.util.event.domain.dto.EventOnStartDTO;
import com.kantboot.util.event.slot.UtilEventSlot;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperPluginOfUtilEventEmitSlot {

    @Resource
    private DeveloperEventRepository eventRepository;

    @Resource
    private DeveloperEventLoggerItemRepository eventLoggerItemRepository;

    @Bean
    public UtilEventSlot utilEventSlot() {
        return new UtilEventSlot() {
            /**
             * 开始调用
             */
            @Override
            public void onStart(EventOnStartDTO eventOnDTO){
                DeveloperEvent eventCache = new DeveloperEvent();
                eventCache.setUuid(eventOnDTO.getUuid());
                eventCache.setCode(eventOnDTO.getCode());
                eventCache.setGmtOnStart(eventOnDTO.getGmtOnStart());
                eventCache.setMethodWithParams(eventOnDTO.getMethodWithParams());
                eventCache.setData(eventOnDTO.getData());
                eventRepository.save(eventCache);
            }

            /**
             * 进行中调用
             */
            @SneakyThrows
            @Override
            public void onInProgress(EventOnInProgressDTO eventOnInProgressDTO){
                DeveloperEventLoggerItem item = new DeveloperEventLoggerItem();
                item.setEventUuid(eventOnInProgressDTO.getUuid());
                item.setLoggerItem(eventOnInProgressDTO.getLoggerItem());
                item.setNanoTime(eventOnInProgressDTO.getLoggerItem().getNanoTime());
                eventLoggerItemRepository.save(item);
            }

            /**
             * 结束调用
             */
            @Override
            public void onEnd(EventOnEndDTO eventOnDTO){
                DeveloperEvent event = eventRepository.findByUuid(eventOnDTO.getUuid());
                if(event==null){
                    OnEndSave(eventOnDTO,0);
                    return;
                }
                event.setGmtOnEnd(eventOnDTO.getGmtOnEnd());
                event.setIsExceptionEnd(eventOnDTO.getIsExceptionEnd());
                event.setException(eventOnDTO.getException());
                eventRepository.save(event);
            }

            @SneakyThrows
            private void OnEndSave(EventOnEndDTO eventOnDTO, int i){
                DeveloperEvent event = eventRepository.findByUuid(eventOnDTO.getUuid());
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
                eventRepository.save(event);
            }


        };
    }

}
