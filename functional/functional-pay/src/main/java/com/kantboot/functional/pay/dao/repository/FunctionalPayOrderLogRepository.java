package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.order.domain.entity.FunctionalPayOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalPayOrderLogRepository extends JpaRepository<FunctionalPayOrderLog, Long> {
}
