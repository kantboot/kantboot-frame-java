package com.kantboot.developer.event.dao.repository;

import com.kantboot.developer.event.domain.entity.DeveloperEventLoggerItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperEventLoggerItemRepository extends JpaRepository<DeveloperEventLoggerItem, Long> {

    List<DeveloperEventLoggerItem> findAllByEventUuid(String eventUuid);

}
