package com.kantboot.util.data.change.controller;

import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.data.change.service.IDataChangeService;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据变化控制器
 */
@AuthInit(name = "对应数据变化",description = "对应数据变化", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/util-data-change-web/dataChange")
public class DataChangeController {

    @Resource
    private IDataChangeService dataChangeService;

    /**
     * 获取对应数据是否变化（根据UUID获取）
     * @param key 数据变化的键
     * @return UUID
     */
    @AuthInit(name = "获取对应数据是否变化（根据UUID获取）",description = "获取对应数据是否变化（根据UUID获取）", sourceLanguageCode = "zh_CN")
    @PostMapping("/getUuidByKey")
    public RestResult<String> getUuidByKey(@RequestParam("key") String key) {
        return RestResult.success(
                dataChangeService.getUuidByKey(key),
                CommonSuccessStateConsts.GET_SUCCESS);
    }

}
