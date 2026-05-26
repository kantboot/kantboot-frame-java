package com.kantboot.in.project.web.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.kantboot.functional.email.dto.EmailMessageDTO;
import com.kantboot.functional.email.service.IFunctionalEmailService;
import com.kantboot.in.project.domain.entity.InProjectTask;
import com.kantboot.in.project.repository.InProjectTaskTypeRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.exception.BaseException;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/in-project-web/admin/task")
public class InProjectTaskControllerOfAdmin
    extends BaseAdminController<InProjectTask,Long> {

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IFunctionalEmailService functionalEmailService;

    @Resource
    private InProjectTaskTypeRepository repository;

    /**
     * 重写save
     */
    @RequestMapping("/save")
    @Override
    public RestResult<?> save(@RequestBody InProjectTask entity) {
        Long selfId = userAccountService.getSelfId();
        entity.setUserAccountOdPublisherId(selfId);
        // 如果有指定受理人，则接收到邮箱
        Long userAccountIdOfAssignee = entity.getUserAccountIdOfAssignee();
        if(userAccountIdOfAssignee!=null){
            UserAccount byId = userAccountService.getById(userAccountIdOfAssignee);
            if(byId!=null&& StrUtil.isNotEmpty(byId.getEmail())){
                Thread.ofVirtual().start(()->{
                    String subject="您有一个新的任务需要处理";
                    String content="您好，您有一个新的任务需要处理，任务标题为："+entity.getTitle()
                        +"，请尽快登录系统查看并处理。";
                    functionalEmailService.send(
                            new EmailMessageDTO()
                                    .setSubject(subject)
                                    .setContent(content)
                    );
                });
            }
        }

        return super.save(entity);
    }

    /**
     * 确认完成
     */
    @RequestMapping("/confirmComplete")
    public RestResult<?> confirmComplete(@RequestBody InProjectTask entity) {
        Optional<InProjectTask> byId = repository.findById(entity.getId());
        if(byId.isEmpty()){
            throw BaseException.of("task.not.found","任务不存在","zh_CN");
        }
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 取消确认完成
     */
    @RequestMapping("/cancelConfirmComplete")
    public RestResult<?> cancelConfirmComplete(@RequestBody InProjectTask entity) {
        Optional<InProjectTask> byId = repository.findById(entity.getId());
        if(byId.isEmpty()){
            throw BaseException.of("task.not.found","任务不存在","zh_CN");
        }
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
