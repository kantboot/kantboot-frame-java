package com.kantboot.system.dict.event;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.data.change.constants.DataChangeCommonKeyConsts;
import com.kantboot.util.data.change.service.IDataChangeService;
import com.kantboot.util.event.annotation.EventOn;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SysDictI18nEventOn {

    @Resource
    private IDataChangeService dataChangeService;

    /**
     * 监听字典的国际化变化
     */
    @EventOn(code = "SysLanguageI18n:save:SysDict")
    public void on(Map<String, String> map) {
        log.info("监听字典的国际化变化:{}", JSON.toJSONString(map));
        dataChangeService.dataChange(DataChangeCommonKeyConsts.CLIENT_INIT);
    }

}
