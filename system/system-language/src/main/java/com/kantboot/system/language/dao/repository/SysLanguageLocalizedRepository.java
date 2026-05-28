package com.kantboot.system.language.dao.repository;

import com.kantboot.system.language.domain.entity.SysLanguageLocalized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLanguageLocalizedRepository
extends JpaRepository<SysLanguageLocalized,Long>
{

    /**
     * 根据code查询
     */
    SysLanguageLocalized findByCode(String code);


}
