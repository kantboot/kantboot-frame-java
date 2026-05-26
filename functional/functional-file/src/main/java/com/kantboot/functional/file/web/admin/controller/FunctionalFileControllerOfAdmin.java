package com.kantboot.functional.file.web.admin.controller;

import com.kantboot.functional.file.domain.entity.FunctionalFile;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "文件管理", description = "文件管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-file-web/admin/file")
public class FunctionalFileControllerOfAdmin
        extends BaseAdminController<FunctionalFile,Long> {

}
