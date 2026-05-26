package com.kantboot.functional.icon.dao.repository;

import com.kantboot.functional.icon.domain.entity.FunctionalIconGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalIconGroupRepository
    extends JpaRepository<FunctionalIconGroup,Long> {
}