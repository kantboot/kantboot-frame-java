package com.kantboot.functional.file.dao.repository;

import com.kantboot.functional.file.domain.entity.FunctionalFileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalFileRecordRepository extends JpaRepository<FunctionalFileRecord,Long> {
}
