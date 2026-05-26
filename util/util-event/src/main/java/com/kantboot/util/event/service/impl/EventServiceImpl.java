package com.kantboot.util.event.service.impl;

import com.kantboot.util.event.domain.dto.EventDTO;
import com.kantboot.util.event.init.EmitInit;
import com.kantboot.util.event.service.IEventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    @Override
    public List<EventDTO> getAll() {
        return EmitInit.EVENT_LIST;
    }
}
