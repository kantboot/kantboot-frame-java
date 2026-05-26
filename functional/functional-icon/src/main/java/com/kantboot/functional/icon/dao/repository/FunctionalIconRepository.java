package com.kantboot.functional.icon.dao.repository;

import com.kantboot.functional.icon.domain.entity.FunctionalIcon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalIconRepository
    extends JpaRepository<FunctionalIcon,Long> {

    /**
     * 根据code查询
     */
    FunctionalIcon findByCode(String code);

}
