package com.kantboot.system.dict.service;

import com.kantboot.system.dict.domain.entity.SysDict;
import com.kantboot.util.base.control.service.IBaseService;

import java.util.List;

public interface ISysDictService
    extends IBaseService<SysDict,Long> {

    /**
     * 根据语言代码和字典组代码获取字典
     * @param dictGroupCode 字典分组编码
     */
    List<SysDict> getDict(String dictGroupCode);

    /**
     * 保存（会让客户端重新初始化）
     * @param entity 实体
     */
    SysDict save(SysDict entity);

    /**
     * 批量保存（会让客户端重新初始化）
     * @param entities 实体列表
     */
    void saveBatch(List<SysDict> entities);

}
