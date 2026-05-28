package com.kantboot.functional.icon.init;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONArray;
import com.kantboot.functional.icon.dao.repository.FunctionalIconGroupRepository;
import com.kantboot.functional.icon.dao.repository.FunctionalIconRepository;
import com.kantboot.functional.icon.domain.entity.FunctionalIcon;
import com.kantboot.functional.icon.domain.entity.FunctionalIconGroup;
import com.kantboot.init.KantbootInit;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FunctionalIconInit {

    private final ResourceLoader resourceLoader;

    public FunctionalIconInit(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Resource
    private FunctionalIconRepository repository;

    @Resource
    private FunctionalIconGroupRepository groupRepository;

    @Resource
    private KantbootInit kantbootInit;


    @PostConstruct
    public void init() {

        if (!kantbootInit.isInit()) {
            log.info("KantbootInit的init属性为false，跳过功能图标初始化");
            return;
        }
        ThreadUtil.execute(() -> {
            // 初始化功能图标分组
            log.info("开始初始化功能图标分组");
            List<FunctionalIconGroup> functionalIconGroups = groupRepository.findAll();
            Map<String, FunctionalIconGroup> groupMap = new HashMap<>();
            for (FunctionalIconGroup functionalIconGroup : functionalIconGroups) {
                groupMap.put(functionalIconGroup.getCode(), functionalIconGroup);
            }
            List<FunctionalIconGroup> groups = getFunctionalIconGroups();
            for (FunctionalIconGroup functionalIconGroup : groups) {
                if (groupMap.get(functionalIconGroup.getCode()) != null) {
//                    log.info("功能图标分组已存在: {}", functionalIconGroup.getCode());
                    continue;
                }
                log.info("保存功能图标分组: {}", functionalIconGroup.getCode());
                groupRepository.save(functionalIconGroup);
            }
            log.info("功能图标分组初始化完成");

            log.info("开始初始化功能图标");
            List<FunctionalIcon> all = repository.findAll();
            Map<String, FunctionalIcon> map = new HashMap<>();
            for (FunctionalIcon functionalIcon : all) {
                map.put(functionalIcon.getCode(), functionalIcon);
            }
            // 获取resource包下的JSON文件
            List<FunctionalIcon> functionalIcons = new ArrayList<>();
            // 添加一些默认的图标
            functionalIcons.add(new FunctionalIcon()
                    .setGroupCode("logo")
                    .setCode("kantboot")
                    .setContent("""
                            <svg>
                                <g transform="translate(0.000000,24.000000) scale(0.0240000,-0.0240000)" stroke="none">
                                    <path d="M30 795 l0 -185 45 0 c37 0 45 3 45 19 0 10 7 25 16 32 16 13 27 5
                            53 -39 5 -9 24 -12 58 -10 l51 3 -48 67 -49 67 46 63 c25 35 64 87 85 116 l39
                            52 -50 0 -50 0 -73 -101 -73 -100 -3 100 -3 101 -44 0 -45 0 0 -185z"></path>
                                    <path d="M400 965 c-12 -15 -5 -55 11 -55 5 0 9 -51 9 -116 0 -101 -3 -121
                            -21 -150 -16 -27 -18 -34 -6 -34 22 0 47 45 47 85 l0 35 60 0 60 0 0 -31 c0
                            -31 45 -119 61 -119 15 0 10 23 -11 55 -17 26 -20 47 -20 153 0 67 4 122 9
                            122 13 0 20 35 9 54 -12 22 -189 24 -208 1z m188 -22 c3 -10 -18 -13 -82 -13
                            -79 0 -97 5 -79 24 13 12 156 2 161 -11z m-30 -114 l-3 -72 -55 0 -55 0 -3 72
                            -3 71 61 0 61 0 -3 -71z"></path>
                                    <path d="M367 923 c-4 -3 -7 -63 -7 -131 0 -69 2 -123 5 -120 5 4 13 258 9
                            258 0 0 -4 -3 -7 -7z"></path>
                                    <path d="M455 615 c-46 -45 -19 -118 43 -118 69 0 99 80 46 122 -35 27 -59 26
                            -89 -4z m79 -8 c37 -27 13 -97 -33 -97 -23 0 -51 31 -51 56 0 45 47 68 84 41z"></path>
                                    <path d="M30 450 l0 -120 30 0 c25 0 30 4 30 23 0 23 25 66 32 57 3 -3 17 -22
                            32 -42 32 -43 50 -47 66 -17 10 17 6 27 -25 65 l-36 44 46 55 47 55 -38 0
                            c-33 0 -44 -7 -81 -47 l-42 -48 -1 48 c0 46 -1 47 -30 47 l-30 0 0 -120z"></path>
                                    <path d="M289 453 l-46 -118 29 -3 c22 -2 34 3 45 17 20 29 88 29 103 1 7 -13
                            21 -20 40 -20 17 0 30 3 30 8 0 4 -19 57 -43 117 -42 109 -43 110 -78 113
                            l-35 3 -45 -118z m98 3 c12 -40 9 -46 -24 -46 -26 0 -27 5 -12 49 14 40 23 39
                            36 -3z"></path>
                                    <path d="M640 503 l0 -68 -26 44 -26 43 -51 -51 -51 -51 14 -42 c9 -24 22 -44
                            32 -46 14 -3 17 7 20 60 l3 63 44 -62 c39 -56 48 -63 77 -63 l34 0 0 96 0 95
                            48 -3 47 -3 3 -92 3 -93 34 0 35 0 0 95 0 95 45 0 c41 0 45 2 45 25 l0 25
                            -165 0 -165 0 0 -67z"></path>
                                    <path d="M30 150 l0 -120 90 0 c77 0 93 3 110 20 24 24 26 63 5 83 -13 13 -13
                            19 -1 42 32 62 -12 95 -126 95 l-78 0 0 -120z m145 45 c0 -15 -8 -21 -34 -23
                            -38 -4 -57 9 -47 33 4 12 16 15 43 13 30 -2 38 -7 38 -23z m5 -80 c19 -22 3
                            -35 -46 -35 -36 0 -44 3 -44 18 0 10 3 22 7 25 12 13 70 7 83 -8z"></path>
                                    <path d="M313 246 c-86 -72 -53 -209 54 -223 45 -6 93 14 117 48 14 21 14 21
                            43 -11 38 -42 102 -51 151 -21 38 25 52 55 52 116 0 66 -39 106 -108 113 -49
                            4 -53 3 -87 -31 l-36 -36 -28 31 c-22 24 -37 32 -77 35 -43 5 -54 2 -81 -21z
                            m93 -34 c16 -11 34 -48 34 -72 0 -29 -44 -71 -67 -64 -28 10 -43 35 -43 71 0
                            49 44 87 76 65z m244 -12 c29 -29 27 -83 -6 -109 -41 -33 -84 -5 -84 56 0 35
                            29 73 55 73 8 0 24 -9 35 -20z"></path>
                                    <path d="M742 243 c3 -25 7 -28 38 -28 l35 -1 3 -92 3 -92 34 0 35 0 0 89 0
                            90 38 3 c32 3 37 6 37 28 l0 25 -113 3 -113 3 3 -28z"></path>
                                </g>
                            </svg>
                            """)
            );
            // 从JSON文件中获取图标
            functionalIcons.addAll(getFunctionalIcons("functional-icon/heroicons.json"));
            functionalIcons.addAll(getFunctionalIcons("functional-icon/remixicon.json"));
            functionalIcons.addAll(getFunctionalIcons("functional-icon/simpleicons.json"));
            functionalIcons.addAll(getFunctionalIcons("functional-icon/element-plus.json"));
            functionalIcons.addAll(getFunctionalIcons("functional-icon/ant.json"));
            functionalIcons.addAll(getFunctionalIcons("functional-icon/iconpark.json"));
            for (FunctionalIcon functionalIcon : functionalIcons) {
                if (map.get(functionalIcon.getCode())!=null) {
//                    log.info("功能图标已存在: {}", functionalIcon.getCode());
                    continue;
                }
                log.info("保存功能图标: {}", functionalIcon.getCode());
                repository.save(functionalIcon);
            }
        });
    }

    public List<FunctionalIcon> getFunctionalIcons(String path) {
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
        return JSONArray.parseArray(json, FunctionalIcon.class);
    }


    public List<FunctionalIconGroup> getFunctionalIconGroups() {
        return List.of(
                new FunctionalIconGroup().setCode("document").setName("文档"),
                new FunctionalIconGroup().setCode("weather").setName("天气"),
                new FunctionalIconGroup().setCode("health").setName("健康"),
                new FunctionalIconGroup().setCode("editor").setName("编辑"),
                new FunctionalIconGroup().setCode("person").setName("人员相关"),
                new FunctionalIconGroup().setCode("communication").setName("沟通"),
                new FunctionalIconGroup().setCode("arrow").setName("箭头"),
                new FunctionalIconGroup().setCode("other").setName("其他"),
                new FunctionalIconGroup().setCode("building").setName("建筑"),
                new FunctionalIconGroup().setCode("system").setName("系统"),
                new FunctionalIconGroup().setCode("device").setName("设备"),
                new FunctionalIconGroup().setCode("design").setName("设计"),
                new FunctionalIconGroup().setCode("map").setName("地图"),
                new FunctionalIconGroup().setCode("logo").setName("标志"),
                new FunctionalIconGroup().setCode("finance").setName("金融"),
                new FunctionalIconGroup().setCode("business").setName("商业"),
                new FunctionalIconGroup().setCode("food").setName("食物"),
                new FunctionalIconGroup().setCode("media").setName("媒体"),
                new FunctionalIconGroup().setCode("development").setName("开发"),
                new FunctionalIconGroup().setCode("face").setName("表情")
        );

    }

}
