package com.kantboot.functional.email.repository;

import com.kantboot.functional.email.domain.entity.FunctionalEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalEmailRepository extends JpaRepository<FunctionalEmail, Long>{
}
