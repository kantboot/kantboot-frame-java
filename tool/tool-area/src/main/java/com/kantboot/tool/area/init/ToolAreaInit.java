package com.kantboot.tool.area.init;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONArray;
import com.kantboot.init.KantbootInit;
import com.kantboot.tool.area.dao.repository.ToolAreaRepository;
import com.kantboot.tool.area.domain.entity.ToolArea;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ToolAreaInit {

    private final ResourceLoader resourceLoader;

    public ToolAreaInit(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Resource
    private KantbootInit kantbootInit;

    @Resource
    private ToolAreaRepository toolAreaRepository;

    @PostConstruct
    public void init() {

        if (!kantbootInit.isInit()) {
            log.info("KantbootInit的init属性为false，跳过国家地区初始化");
            return;
        }

        ThreadUtil.execute(() -> {

            List<ToolArea> list = getToolAreaList("tool-area/ToolAreaCountryRegion.init.json");
            if (list.isEmpty()) {
                log.info("没有找到地区数据，跳过初始化");
                return;
            }
            List<ToolArea> listAll = toolAreaRepository.findAll();
            Map<String, ToolArea> countryRegionMap = listAll.stream()
                    .collect(java.util.stream.Collectors.toMap(ToolArea::getCode, item -> item));
            // 如果存在则不保存
            for (ToolArea area : list) {
                if (countryRegionMap.get(area.getCode()) != null) {
                    continue;
                }
                area.setLevel(0);
                toolAreaRepository.save(area);
                log.info("保存地区: {}", area.getCode());
            }

            list = new ArrayList<>();
            list.addAll(getToolAreaList("tool-area/ToolAreaCHN.init.json"));
            list.addAll(getToolAreaList("tool-area/ToolAreaUSA.init.json"));
            if (list.isEmpty()) {
                log.info("没有找到地区数据，跳过初始化");
                return;
            }

            int size = list.size();
            for(int i = 0; i < list.size(); i++) {
                ToolArea area = list.get(i);
                if (countryRegionMap.get(area.getCode()) != null) {
                    continue;
                }
                toolAreaRepository.save(area);
                log.info("保存地区: {},{},{}/{}", area.getCode(),area.getName(), i + 1, size);
            }
        });
    }

    public List<ToolArea> getToolAreaList(String path) {
        // 从resource包下获取JSON文件
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);
        if (!resource.exists()) {
            log.error("资源文件不存在: {}", path);
            return List.of();
        }
        byte[] bytes = null;
        try {
            bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String json = new String(bytes);
        return JSONArray.parseArray(json, ToolArea.class);
    }


}
