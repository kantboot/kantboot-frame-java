package com.kantboot.user.location.dao.repository;

import com.kantboot.user.location.domain.entity.UserAccountLocationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountLocationLogRepository extends JpaRepository<UserAccountLocationLog,Long> {
}
