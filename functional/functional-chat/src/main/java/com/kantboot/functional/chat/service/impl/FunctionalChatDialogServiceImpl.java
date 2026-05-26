package com.kantboot.functional.chat.service.impl;

import com.kantboot.functional.chat.consts.DialogTypeConsts;
import com.kantboot.functional.chat.dao.repository.*;
import com.kantboot.functional.chat.domain.dto.DialogCreateDTO;
import com.kantboot.functional.chat.domain.dto.DialogSearchDTO;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialog;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogDelete;
import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountDialogRelationship;
import com.kantboot.functional.chat.service.IFunctionalChatDialogPersonService;
import com.kantboot.functional.chat.service.IFunctionalChatDialogService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionalChatDialogServiceImpl
    implements IFunctionalChatDialogService {

    @Resource
    private FunctionalChatDialogRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IFunctionalChatDialogPersonService dialogPersonService;

    @Resource
    private FunctionalChatUserAccountDialogRelationshipRepository userAccountDialogRelationshipRepository;

    @Resource
    private FunctionalChatDialogDeleteRepository deleteRepository;

    @Override
    public FunctionalChatDialog create(DialogCreateDTO dto) {
        if(dto.getUserAccountIds()!=null&&dto.getUserAccountIds().size()<=1){
            // 提示无法创建群聊
            throw BaseException.of("notSupported","不支持创建无人群聊","zh_CN");
        }
        if(DialogTypeConsts.ONE_TO_ONE.equals(dto.getType())){
            Long userAccountId1 = dto.getUserAccountIds().get(0);
            Long userAccountId2 = dto.getUserAccountIds().get(1);
            // 生成唯一标识符，保证userId1小于userId2
            String identifier;
            if(userAccountId1 < userAccountId2){
                identifier = userAccountId1 + "&" + userAccountId2;
            }else{
                identifier = userAccountId2 + "&" + userAccountId1;
            }
            // 检查是否存在该私聊会话
            FunctionalChatDialog existingDialog = repository.findByOneToOneIdentifier(identifier);
            if(existingDialog != null){
                return existingDialog;
            }
            // 创建新的私聊会话
            FunctionalChatDialog dialog = new FunctionalChatDialog();
            dialog.setType(DialogTypeConsts.ONE_TO_ONE);
            dialog.setOneToOneIdentifier(identifier);
            dialog.setUserAccountId1OfOneToOne(userAccountId1);
            dialog.setUserAccountId2OfOneToOne(userAccountId2);
            FunctionalChatDialog save = repository.save(dialog);
            dialogPersonService.addUserAccounts(dialog.getId(),List.of(userAccountId1,userAccountId2));
            return save;
        }
        return null;
    }

    @Override
    public FunctionalChatDialog toOneToOne(Long userAccountId) {
        Long selfId = userAccountService.getSelfId();
        DialogCreateDTO dto = new DialogCreateDTO();
        dto.setType(DialogTypeConsts.ONE_TO_ONE);
        List<Long> list = new ArrayList();
        list.add(selfId);
        list.add(userAccountId);
        dto.setUserAccountIds(list);
        return create(dto);
    }

    @Override
    public List<FunctionalChatDialog> getBy(DialogSearchDTO dto) {
        Long selfId = userAccountService.getSelfId();
        PageParam<DialogSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageSize(300);
        pageParam.setPageNumber(1);
        Page<FunctionalChatDialog> by = repository.getBy(dto, pageParam.getPageable());
        List<FunctionalChatDialog> content = new ArrayList<>(by.getContent());
        List<String> codes = new ArrayList<>();
        for (FunctionalChatDialog functionalChatDialog : content) {
            codes.add(selfId+"&"+functionalChatDialog.getId());
        }
        List<FunctionalChatUserAccountDialogRelationship> firstByCodeIn = userAccountDialogRelationshipRepository.findFirstByCodeIn(codes);
        for (int i = 0; i < firstByCodeIn.size(); i++) {
            content.get(i).setUnreadCount(firstByCodeIn.get(i).getUnreadCount());
        }

        // 不显示
        List<FunctionalChatDialogDelete> byCodeIn = deleteRepository.findByCodeIn(codes);
        for (FunctionalChatDialogDelete functionalChatDialogDelete : byCodeIn) {
            if(functionalChatDialogDelete.getDialogId()==null){
                continue;
            }
            if (functionalChatDialogDelete.getIsReleased()==true) {
                continue;
            }
            for (int i = 0; i < content.size(); i++) {
                if(content.get(i).getId().equals(functionalChatDialogDelete.getDialogId())){
                    content.remove(i);
                    break;
                }
            }
        }

        return content;
    }

    @Override
    public FunctionalChatDialog getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<FunctionalChatDialog> getBySelf(DialogSearchDTO dto) {
        Long selfId = userAccountService.getSelfId();
        dto.setUserAccountId(selfId);
        return getBy(dto);
    }

    @Override
    public void deleteBySelf(Long dialogId) {
        Long selfId = userAccountService.getSelfId();
        FunctionalChatDialog byId = getById(dialogId);
        String code = selfId + "&" + dialogId;
        FunctionalChatDialogDelete entity = deleteRepository.findByCode(code);
        if(entity==null){
            entity = new FunctionalChatDialogDelete();
        }
        entity.setUserAccountId(selfId);
        entity.setDialogId(dialogId);
        entity.setCode(code);
        entity.setIsReleased(false);
        entity.setLastMessageId(byId.getLastMessageId());
        deleteRepository.save(entity);
    }

}
