package com.kantboot.tool.area.web.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.tool.area.domain.entity.ToolArea;
import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tool-area-web/admin/area")
public class ToolAreaControllerOfAdmin
    extends BaseAdminController<ToolArea,Long> {
}
