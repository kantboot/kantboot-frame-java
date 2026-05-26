package com.kantboot.functional.chat.service;

import com.kantboot.functional.chat.domain.dto.DialogMessageDTO;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogMessage;

import java.util.List;

public interface IFunctionalChatDialogMessageService {

    FunctionalChatDialogMessage getById(Long id);

    void send(DialogMessageDTO dto);

    List<FunctionalChatDialogMessage> getMaxList(Long dialogId,Long minId);

}
