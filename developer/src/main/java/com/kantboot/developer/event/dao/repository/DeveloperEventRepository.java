package com.kantboot.developer.event.dao.repository;

import com.kantboot.developer.event.domain.entity.DeveloperEvent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeveloperEventRepository extends JpaRepository<DeveloperEvent, Long> {

    DeveloperEvent findByUuid(String uuid);

}
