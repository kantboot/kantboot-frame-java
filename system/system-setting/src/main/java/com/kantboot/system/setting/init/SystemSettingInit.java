package com.kantboot.system.setting.init;

import cn.hutool.core.util.StrUtil;
import com.kantboot.init.KantbootInit;
import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.domain.entity.SysSettingGroup;
import com.kantboot.system.setting.properties.SystemSettingProperties;
import com.kantboot.system.setting.service.ISysSettingGroupService;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class SystemSettingInit {

    @Resource
    private ISysSettingService sysSettingService;

    @Resource
    private ISysSettingGroupService sysSettingGroupService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SystemSettingProperties properties;

    @Resource
    private KantbootInit kantbootInit;

    public String getValueByPropertiesCode(String code) {
        String[] codeSplit = code.split("\\.");
        if (codeSplit.length != 2) {
            log.error("设置项编码格式错误，正确格式为：分组编码.设置项编码");
            return null;
        }
        Map<String, Map<String, Object>> settings = properties.getSettings();
        if (settings == null || settings.isEmpty()) {
            log.warn("系统设置属性未配置或为空");
            return null;
        }
        Map<String, Object> stringObjectMap = settings.get(codeSplit[0]);
        if (stringObjectMap == null) {
            log.warn("分组编码不存在");
            return null;
        }
        return String.valueOf(stringObjectMap.get(codeSplit[1]));
    }

    @PostConstruct
    public void init() {
        // 如果不需要初始化，则直接返回
        if (!kantbootInit.isInit()) {
            log.info("系统设置初始化被禁用");
            return;
        }



        // 获取所有带有@SettingGroup注解的Bean
        Map<String, Object> settingGroups = applicationContext.getBeansWithAnnotation(SettingGroup.class);
        settingGroups.forEach((beanName, bean) -> {
            // 获取原始类
            Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
            log.debug("Processing bean: {}, original class: {}", beanName, clazz.getName());

            Optional<SettingGroup> groupAnnotation = AnnotationUtils.findAnnotation(clazz, SettingGroup.class);

            if (groupAnnotation.isEmpty()) {
                log.error("类{}缺少@SettingGroup注解", clazz.getName());
                return;
            }

            // 获取分组元数据
            String groupCode = groupAnnotation.get().code();
            String groupName = groupAnnotation.get().name();
            String groupDescription = groupAnnotation.get().description();
            String languageCode = groupAnnotation.get().sourceLanguageCode();

            log.debug("Initializing setting group: {}", groupCode);

            // 初始化分组元数据
            sysSettingGroupService.init(
                    List.of(new SysSettingGroup()
                            .setCode(groupCode)
                            .setName(groupName)
                            .setDescription(groupDescription)
                            .setSourceLanguageCode(languageCode))
            );

            // 遍历所有字段
            for (Field field : clazz.getDeclaredFields()) {
                Setting settingAnnotation = field.getAnnotation(Setting.class);
                if (settingAnnotation != null) {
                    field.setAccessible(true);

                    // 构建设置项的唯一标识（如：test.apiCode）
                    String settingCode = settingAnnotation.code();
                    String fullCode = groupCode + "." + settingCode;

                    log.debug("Processing setting: {}", fullCode);

                    String value = getValueByPropertiesCode(fullCode);
                    if(StrUtil.isEmpty(value)){
                        value = settingAnnotation.defaultValue();
                    }

                    // 初始化设置项
                    sysSettingService.init(
                            List.of(new SysSetting()
                                    .setGroupCode(groupCode)
                                    .setCode(fullCode)
                                    .setValue(value)
                                    .setName(settingAnnotation.name())
                                    .setDescription(settingAnnotation.description())
                                    .setSourceLanguageCode(settingAnnotation.sourceLanguageCode()))
                    );
                }
            }
        });
    }
}