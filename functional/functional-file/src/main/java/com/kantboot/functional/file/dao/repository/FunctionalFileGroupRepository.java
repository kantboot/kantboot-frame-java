package com.kantboot.functional.file.dao.repository;


import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalFileGroupRepository extends JpaRepository<FunctionalFileGroup, Long>
{

    /**
     * 根据文件组编码获取文件组
     * @param code 文件组编码
     * @return 文件组
     */
    FunctionalFileGroup findByCode(String code);

    /**
     * 判断文件组是否存在
     * @param code 文件组编码
     * @return 是否存在
     */
    Boolean existsByCode(String code);

}
