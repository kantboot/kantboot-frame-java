package com.kantboot.functional.chat.web.controller;

import com.kantboot.functional.chat.domain.dto.DialogCreateDTO;
import com.kantboot.functional.chat.domain.dto.DialogSearchDTO;
import com.kantboot.functional.chat.service.IFunctionalChatDialogService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "对话", description = "对话", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-chat-web/dialog")
public class FunctionalChatDialogController {

    @Resource
    private IFunctionalChatDialogService service;

    /**
     * 创建对话
     */
    @AuthInit(name = "创建对话", description = "创建对话", allPass = true)
    @RequestMapping("/create")
    public RestResult<?> create(@RequestBody DialogCreateDTO dto) {
        return RestResult.success(service.create(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name = "创建一对一对话", description = "创建一对一对话", allPass = true)
    @RequestMapping("/toOneToOne")
    public RestResult<?> toOneToOne(@RequestParam("userAccountId") Long userAccountId) {
        return RestResult.success(service.toOneToOne(userAccountId), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name="根据ID获取对话",description = "根据ID获取对话",allPass = true)
    @RequestMapping("/getById")
    public RestResult<?> getById(@RequestParam("id") Long id){
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取对话列表", description = "获取对话列表", allPass = true)
    @RequestMapping("/getBySelf")
    public RestResult<?> getBySelf(@RequestBody DialogSearchDTO dto) {
        return RestResult.success(service.getBySelf(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 删除对话
     */
    @AuthInit(name = "删除对话", description = "删除对话", allPass = true)
    @RequestMapping("/deleteBySelf")
    public RestResult<?> deleteBySelf(@RequestParam("dialogId") Long dialogId) {
        service.deleteBySelf(dialogId);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }
}
