package com.kantboot.functional.chat.service.impl;

import com.kantboot.functional.chat.dao.repository.FunctionalChatDialogMessageRepository;
import com.kantboot.functional.chat.dao.repository.FunctionalChatReadRepository;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogMessage;
import com.kantboot.functional.chat.domain.entity.FunctionalChatRead;
import com.kantboot.functional.chat.service.IFunctionalChatDialogPersonService;
import com.kantboot.functional.chat.service.IFunctionalChatReadService;
import com.kantboot.functional.chat.service.IFunctionalChatUserAccountRelationshipService;
import com.kantboot.user.account.service.IUserAccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionalChatReadServiceImpl
        implements IFunctionalChatReadService {

    @Resource
    private IFunctionalChatDialogPersonService personService;

    @Resource
    private FunctionalChatDialogMessageRepository functionalChatDialogMessageRepository;

    @Resource
    private FunctionalChatReadRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IFunctionalChatUserAccountRelationshipService relationshipService;

    @Override
    public void markAsUnread(Long messageId) {
        FunctionalChatDialogMessage byId = functionalChatDialogMessageRepository.findById(messageId).orElse(null);
        Long dialogId = byId.getDialogId();
        Long selfId = userAccountService.getSelfId();

        List<Long> userAccountIdsByDialogId = personService.getUserAccountIdsByDialogId(dialogId);
        for (Long userAccountId : userAccountIdsByDialogId) {
            if(userAccountId.equals(selfId)){
                continue;
            }
            FunctionalChatRead functionalChatRead = new FunctionalChatRead();
            functionalChatRead.setRead(false);
            functionalChatRead.setUserAccountId(userAccountId);
            functionalChatRead.setDialogId(dialogId);
            functionalChatRead.setMessageId(messageId);
            repository.save(functionalChatRead);
            relationshipService.computeRelationship(userAccountId,dialogId);
        }
    }

    @Override
    public void markAsRead(Long dialogId, Long userAccountId) {
        repository.updateReadByDialogIdAndUserAccountId(dialogId, userAccountId);
        relationshipService.computeRelationship(userAccountId, dialogId);
    }

    @Override
    public void markAsReadSelf(Long dialogId) {
        Long selfId = userAccountService.getSelfId();
        markAsRead(dialogId, selfId);
    }
}
