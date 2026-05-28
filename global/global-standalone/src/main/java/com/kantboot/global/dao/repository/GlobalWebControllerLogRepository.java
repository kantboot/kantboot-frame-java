package com.kantboot.global.dao.repository;

import com.kantboot.global.domain.entity.GlobalWebControllerLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalWebControllerLogRepository
    extends JpaRepository<GlobalWebControllerLog, Long> {
}
