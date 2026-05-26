package com.kantboot.util.event.service;

import com.kantboot.util.event.domain.dto.EventDTO;

import java.util.List;

public interface IEventService {

    /**
     * 查询所有事件
     */
    List<EventDTO> getAll();

}