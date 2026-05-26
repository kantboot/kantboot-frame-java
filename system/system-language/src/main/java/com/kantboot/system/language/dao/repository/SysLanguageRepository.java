package com.kantboot.system.language.dao.repository;

import com.kantboot.system.language.domain.entity.SysLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典国际化数据访问层接口
 * @author 方某方
 */
@Repository
public interface SysLanguageRepository extends JpaRepository<SysLanguage, Long> {

    /**
     * 查询所有支持的语言
     * @return 语言列表
     */
    @Query("FROM SysLanguage language WHERE language.support = true")
    List<SysLanguage> findBySupportTrue();

}
