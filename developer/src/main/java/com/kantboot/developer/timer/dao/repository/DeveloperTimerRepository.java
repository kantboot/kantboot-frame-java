package com.kantboot.developer.timer.dao.repository;

import com.kantboot.developer.event.domain.entity.DeveloperEvent;
import com.kantboot.developer.timer.domain.entity.DeveloperTimer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeveloperTimerRepository extends JpaRepository<DeveloperTimer, Long> {

    DeveloperTimer findByUuid(String uuid);

}
