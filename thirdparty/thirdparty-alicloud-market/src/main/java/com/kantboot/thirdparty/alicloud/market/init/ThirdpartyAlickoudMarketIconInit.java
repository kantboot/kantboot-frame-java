package com.kantboot.thirdparty.alicloud.market.init;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONArray;
import com.kantboot.init.KantbootInit;
import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequest;
import com.kantboot.thirdparty.alicloud.market.dao.repository.ThirdpartyAlicloudMarketRequestRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ThirdpartyAlickoudMarketIconInit {

    private final ResourceLoader resourceLoader;

    public ThirdpartyAlickoudMarketIconInit(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Resource
    private ThirdpartyAlicloudMarketRequestRepository repository;

    @Resource
    private KantbootInit kantbootInit;


    @Transactional
    @PostConstruct
    public void init() {

        if (!kantbootInit.isInit()) {
            log.info("KantbootInit的init属性为false，跳过阿里云市场API初始化");
            return;
        }
        Thread.ofVirtual()
                .name("SystemLanguageInit")
                .start(()->{
            // 初始化功能图标分组
            log.info("开始初阿里云市场API");

            log.info("开始初始化功能图标");
            List<ThirdpartyAlicloudMarketRequest> all = repository.findAll();
            Map<String, ThirdpartyAlicloudMarketRequest> map = new HashMap<>();
            for (ThirdpartyAlicloudMarketRequest alicloudmarketRequest : all) {
                map.put(alicloudmarketRequest.getCode(), alicloudmarketRequest);
            }
            // 获取resource包下的JSON文件
            List<ThirdpartyAlicloudMarketRequest> entityList = new ArrayList<>();
            entityList.addAll(getByFile("thirdparty-alicloud-market/init.json"));
            for (ThirdpartyAlicloudMarketRequest marketRequest : entityList) {
                if (map.get(marketRequest.getCode())!=null) {
                    log.info("功阿里云市场已存在: {}", marketRequest.getCode());
                    continue;
                }
                log.info("保存阿里云市场API: {}", marketRequest.getCode());
                repository.save(marketRequest);
            }
        });
    }

    public List<ThirdpartyAlicloudMarketRequest> getByFile(String path) {
        // 从resource包下获取JSON文件
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:"+path);
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
        return JSONArray.parseArray(json, ThirdpartyAlicloudMarketRequest.class);
    }

}
