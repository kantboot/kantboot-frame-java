package com.kantboot.tool.ip.web.admin.controller;

import com.kantboot.tool.ip.domain.entity.ToolIp;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool-ip-web/admin/ip")
public class ToolIpControllerOfAdmin
    extends BaseAdminController<ToolIp,Long> {

}
