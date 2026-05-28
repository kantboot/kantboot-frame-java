package com.kantboot.system.setting.web.admin.controller;

import com.kantboot.system.setting.domain.entity.SysSettingGroup;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统设置分组管理控制器
 * 提供对系统设置分组的管理功能
 * 继承自BaseAdminController，提供基本的CRUD操作
 */
@AuthInit(name = "系统设置分组管理", description = "系统设置分组管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-setting-web/admin/settingGroup")
public class SysSettingGroupControllerOfAdmin extends BaseAdminController<SysSettingGroup, String> {

}
