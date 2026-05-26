package com.kantboot.functional.chat.service.impl;

import com.kantboot.functional.chat.dao.repository.FunctionalChatReadRepository;
import com.kantboot.functional.chat.dao.repository.FunctionalChatUserAccountDialogRelationshipRepository;
import com.kantboot.functional.chat.dao.repository.FunctionalChatUserAccountRelationshipRepository;
import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountDialogRelationship;
import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountRelationship;
import com.kantboot.functional.chat.service.IFunctionalChatUserAccountRelationshipService;
import com.kantboot.user.account.service.IUserAccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FunctionalChatUserAccountRelationshipServiceImpl
    implements IFunctionalChatUserAccountRelationshipService
{

    @Resource
    private FunctionalChatUserAccountRelationshipRepository repository;

    @Resource
    private FunctionalChatReadRepository functionalChatReadRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private FunctionalChatUserAccountDialogRelationshipRepository dialogRelationshipRepository;

    @Override
    public void computeRelationship(Long userAccountId) {
        FunctionalChatUserAccountRelationship byUserAccountId = repository.findFirstByUserAccountId(userAccountId);
        Long unreadCount = functionalChatReadRepository.countByUserAccountIdAndRead(userAccountId, false);
        if(byUserAccountId==null){
            byUserAccountId=new FunctionalChatUserAccountRelationship();
            byUserAccountId.setUserAccountId(userAccountId);
        }
        byUserAccountId.setUnreadCount(unreadCount);
        repository.save(byUserAccountId);
    }

    @Override
    public void computeRelationship(Long userAccountId, Long dialogId) {
        computeRelationship(userAccountId);
        Long unreadCount = functionalChatReadRepository.countByUserAccountIdAndDialogIdAndRead(userAccountId, dialogId, false);
        FunctionalChatUserAccountDialogRelationship firstByUserAccountIdAndDialogId = dialogRelationshipRepository.findFirstByUserAccountIdAndDialogId(userAccountId, dialogId);
        if(firstByUserAccountIdAndDialogId==null){
            firstByUserAccountIdAndDialogId=new FunctionalChatUserAccountDialogRelationship();
            firstByUserAccountIdAndDialogId.setUserAccountId(userAccountId);
            firstByUserAccountIdAndDialogId.setDialogId(dialogId);
            firstByUserAccountIdAndDialogId.setCode(userAccountId+"&"+dialogId);
        }
        firstByUserAccountIdAndDialogId.setUnreadCount(unreadCount);
        dialogRelationshipRepository.save(firstByUserAccountIdAndDialogId);
    }

    @Override
    public FunctionalChatUserAccountRelationship getByUserAccountId(Long userAccountId) {
        FunctionalChatUserAccountRelationship firstByUserAccountId = repository.findFirstByUserAccountId(userAccountId);
        if(firstByUserAccountId==null){
            computeRelationship(userAccountId);
            firstByUserAccountId = repository.findFirstByUserAccountId(userAccountId);
        }
        return firstByUserAccountId;
    }

    public FunctionalChatUserAccountRelationship getBySelf() {
        Long selfId = userAccountService.getSelfId();
        return getByUserAccountId(selfId);
    }
}
