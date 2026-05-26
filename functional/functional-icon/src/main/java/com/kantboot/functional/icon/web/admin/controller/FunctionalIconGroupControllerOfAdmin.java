package com.kantboot.functional.icon.web.admin.controller;

import com.kantboot.functional.icon.domain.entity.FunctionalIconGroup;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/functional-icon-web/admin/iconGroup")
public class FunctionalIconGroupControllerOfAdmin
    extends BaseAdminController<FunctionalIconGroup,Long> {

}
