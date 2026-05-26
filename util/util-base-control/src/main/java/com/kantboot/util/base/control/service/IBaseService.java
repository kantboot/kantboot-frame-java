package com.kantboot.util.base.control.service;

import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseService<T extends Serializable,ID> {

    /**
     * 通用查询
     * 查询所有
     */
    List<T> getAll(ConditionGlobeEntity operatorGlobe, Class<T> tClass);

    List<T> getAllEasy(Map<String, Object> operatorGlobe, Class<T> tClass);

    /**
     * 通用查询
     * 分页查询
     */
    PageResult getBodyData(PageParam<ConditionGlobeEntity> pageParam, Class<T> tClass);

    PageResult getBodyDataEasy(PageParam<Map<String, Object>> pageParam, Class<T> tClass);

    /**
     * 添加数据
     */
    T save(T entity,Class<T> tClass);

    /**
     * 批量保存
     */
    void saveBatch(List<T> entityList,Class<T> tClass);


    /**
     * 根据id删除
     */
    void remove(T entity,Class<T> tClass);

    /**
     * 批量删除
     */
    void removeBatch(List<T> entityList,Class<T> tClass);

    /**
     * 根据id获取
     */
    T getById(ID id,Class<T> tClass);

    T saveI18n(String languageCode, String attr,String value,ID id,Class<T> tClass);

}
