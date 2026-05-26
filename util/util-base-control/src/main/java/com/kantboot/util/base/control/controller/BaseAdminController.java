package com.kantboot.util.base.control.controller;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import com.kantboot.util.base.control.service.impl.BaseServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@AuthInit(name = "通用后台管理模板", description = "通用后台管理模板", sourceLanguageCode = "zh_CN")
@RestController
public class BaseAdminController<T extends Serializable, ID> {

    @Resource
    private BaseServiceImpl<T, ID> service;

    /**
     * 获取第一个泛型的类型
     */
    private Class<T> getTClass() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            // 强制不检查转换
            // noinspection unchecked
            return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }

    @AuthInit(name = "获取所有（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/getAll")
    public RestResult<?> getAll(@RequestBody ConditionGlobeEntity param) {
        return RestResult.success(service.getAll(param, getTClass()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取所有（通用后台管理）（简单版）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/getAllEasy")
    public RestResult<?> getAllEasy(@RequestBody Map<String, Object> param) {
        return RestResult.success(service.getAllEasy(param, getTClass()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取所有（通用后台管理）（分页）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/getBodyData")
    public RestResult<?> getBodyData(@RequestBody PageParam<ConditionGlobeEntity> pageParam) {
        return RestResult.success(service.getBodyData(pageParam, getTClass()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取所有（通用后台管理）（分页）（简单版）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/getBodyDataEasy")
    public RestResult<?> getBodyDataEasy(@RequestBody PageParam<Map<String, Object>> pageParam) {
        return RestResult.success(service.getBodyDataEasy(pageParam, getTClass()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "保存（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/save")
    public RestResult<?> save(@RequestBody T t) {
        return RestResult.success(
                JSON.parseObject(JSON.toJSONString(service.save(t, getTClass())), getTClass())
                , CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "批量保存（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/saveBatch")
    public RestResult<?> saveBatch(@RequestBody List<T> tList) {
        service.saveBatch(tList, getTClass());
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "删除（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/remove")
    public RestResult<?> remove(@RequestBody T t) {
        service.remove(t, getTClass());
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @AuthInit(name = "批量删除（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/removeBatch")
    public RestResult<?> removeBatch(@RequestBody List<T> tList) {
        service.removeBatch(tList, getTClass());
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    /**
     * 根据id获取
     */
    @AuthInit(name = "根据id获取（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/getById")
    public RestResult<?> getById(@RequestParam("id") ID id) {
        return RestResult.success(service.getById(id, getTClass()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 保存国际化信息
     */
    @AuthInit(name = "保存国际化信息（通用后台管理）", description = "通用后台管理", sourceLanguageCode = "zh_CN")
    @PostMapping("/saveI18n")
    public RestResult<?> saveI18n(@RequestParam("targetLanguageCode") String targetLanguageCode,
                                  @RequestParam("attr") String attr,
                                  @RequestParam("id") ID id,
                                  @RequestParam("value") String value
                                  ) {
        return RestResult.success(service.saveI18n(targetLanguageCode, attr, value,id, getTClass()), CommonSuccessStateConsts.SAVE_SUCCESS);
    }
}
