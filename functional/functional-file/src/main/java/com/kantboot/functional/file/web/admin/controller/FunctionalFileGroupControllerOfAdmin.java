package com.kantboot.functional.file.web.admin.controller;

import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;
import com.kantboot.functional.file.service.IFunctionalFileGroupService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件
 * 用于管理文件的上传、下载、删除等
 */
@AuthInit(name = "文件组管理",description = "文件组管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-file-web/admin/fileGroup")
public class FunctionalFileGroupControllerOfAdmin
    extends BaseAdminController<FunctionalFileGroup,Long> {

    @Resource
    private IFunctionalFileGroupService service;

    @Override
    @AuthInit(name = "保存（重写通用后台管理）",description = "重写通用后台管理", sourceLanguageCode = "zh_CN")
    @RequestMapping("/save")
    public RestResult<?> save(FunctionalFileGroup entity) {
        return RestResult.success(service.save(entity), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

}
