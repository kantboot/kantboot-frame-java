package com.kantboot.functional.file.service;


import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;

/**
 * 文件组管理的Service接口
 * @author FangMoFang
 */
public interface IFunctionalFileGroupService {

    /**
     * 根据文件组编码获取文件组
     * @param code 文件组编码
     * @return 文件组
     */
    FunctionalFileGroup getByCode(String code);

    /**
     * 根据文件组编码获取文件组路径
     * @param code 文件组编码
     * @return 文件组路径
     */
    String getPathByCode(String code);

    /**
     * 保存文件组
     * @param group 文件组
     */
    FunctionalFileGroup save(FunctionalFileGroup group);

}
