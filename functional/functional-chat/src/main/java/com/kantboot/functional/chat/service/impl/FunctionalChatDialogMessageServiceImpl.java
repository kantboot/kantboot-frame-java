package com.kantboot.functional.chat.service.impl;

import com.kantboot.functional.chat.dao.repository.FunctionalChatDialogDeleteRepository;
import com.kantboot.functional.chat.dao.repository.FunctionalChatDialogMessageRepository;
import com.kantboot.functional.chat.dao.repository.FunctionalChatDialogRepository;
import com.kantboot.functional.chat.domain.dto.DialogMessageDTO;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialog;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogDelete;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogMessage;
import com.kantboot.functional.chat.service.*;
import com.kantboot.user.account.service.IUserAccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionalChatDialogMessageServiceImpl
    implements IFunctionalChatDialogMessageService {

    @Resource
    private IFunctionalChatDialogService dialogService;

    @Resource
    private FunctionalChatDialogMessageRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private FunctionalChatDialogRepository dialogRepository;

    @Resource
    private IFunctionalChatReadService functionalChatReadService;

    @Resource
    private IFunctionalChatDialogPersonService dialogPersonService;

    @Resource
    private FunctionalChatDialogDeleteRepository deleteRepository;


    @Override
    public FunctionalChatDialogMessage getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void send(DialogMessageDTO dto) {
        Long selfId = userAccountService.getSelfId();
        FunctionalChatDialogMessage message = new FunctionalChatDialogMessage();
        message.setDialogId(dto.getDialogId());
        message.setKtFormatOfView(dto.getKtFormatOfView());
        message.setTextContent(dto.getTextContent());
        message.setUserAccountId(selfId);
        message.setVirtualId(dto.getVirtualId());
        FunctionalChatDialog byId = dialogService.getById(dto.getDialogId());
        // 接收者
        Long receiverId = null;
        if(!(byId.getUserAccountId1OfOneToOne().equals(selfId))){
            receiverId = byId.getUserAccountId1OfOneToOne();
        } else {
            receiverId = byId.getUserAccountId2OfOneToOne();
        }
        message.setUserAccountIdOfReceiver(receiverId);
        FunctionalChatDialogMessage save = repository.save(message);
        byId.setLastTextMessage(dto.getTextContent());
        byId.setLastMessageId(save.getId());
        dialogRepository.save(byId);
        functionalChatReadService.markAsUnread(save.getId());

        Thread.ofVirtual().name("FunctionalChatDialogMessageServiceImpl-send-emit",0).start(()->{
            List<Long> userAccountIdsByDialogId = dialogPersonService.getUserAccountIdsByDialogId(dto.getDialogId());
            for (Long userAccountId : userAccountIdsByDialogId) {
                FunctionalChatDialogDelete byCode = deleteRepository.findByCode(userAccountId + "&" + dto.getDialogId());
                if(byCode!=null){
                    byCode.setIsReleased(true);
                    deleteRepository.save(byCode);
                }
            }
        });

    }

    @Override
    public List<FunctionalChatDialogMessage> getMaxList(Long dialogId,Long minId) {
        Long selfId = userAccountService.getSelfId();
        FunctionalChatDialogDelete byCode = deleteRepository.findByCode(selfId + "&" + dialogId);
        if(byCode!=null){
            Long lastMessageId = byCode.getLastMessageId();
            if (lastMessageId!=null&&minId<lastMessageId){
                minId = lastMessageId;
            }
        }

        return repository.findByDialogIdAndMinId(dialogId,minId);
    }
}
