package com.kantboot.functional.file.dao.repository;

import com.kantboot.functional.file.domain.entity.FunctionalFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalFileRepository extends JpaRepository<FunctionalFile, Long> {

    /**
     * 根据md5和文件组编码获取文件
     *
     * @param md5       文件md5
     * @param groupCode 文件组编码
     * @return 文件
     */
    List<FunctionalFile> findByMd5AndGroupCode(String md5, String groupCode);

    /**
     * 根据md5和文件组编码获取第一个
     *
     * @param md5       文件md5
     * @param groupCode 文件组编码
     * @return 文件
     */
    FunctionalFile findFirstByMd5AndGroupCode(String md5, String groupCode);


    /**
     * 根据分组编码和编码获取文件
     *
     * @param groupCode 文件组编码
     * @param code      文件编码
     * @return 文件
     */
    FunctionalFile findByGroupCodeAndCode(String groupCode, String code);

    /**
     * 根据分组获取文件
     *
     * @param groupCode 文件组编码
     * @return 文件
     */
    List<FunctionalFile> findByGroupCode(String groupCode);

    /**
     * 根据路径获取文件
     */
    FunctionalFile findFirstByPath(String path);

    /**
     * 根据全编码获取文件
     */
    FunctionalFile findFirstByFullCode(String fullCode);

}
