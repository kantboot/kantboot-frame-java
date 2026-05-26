package com.kantboot.developer.timer.dao.repository;

import com.kantboot.developer.event.domain.entity.DeveloperEventLoggerItem;
import com.kantboot.developer.timer.domain.entity.DeveloperTimerLoggerItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperTimerLoggerItemRepository extends JpaRepository<DeveloperTimerLoggerItem, Long> {

    List<DeveloperTimerLoggerItem> findAllByTimerUuid(String timerUuid);

}
