package com.kantboot.in.project.repository;

import com.kantboot.in.project.domain.entity.InProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InProjectTaskTypeRepository
    extends JpaRepository<InProjectTask,Long> {
}
