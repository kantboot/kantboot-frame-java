package com.kantboot.functional.chat.service;

import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogPerson;

import java.util.List;

public interface IFunctionalChatDialogPersonService {

    void addUserAccounts(Long dialogId,List<Long> userAccountIds);

    /**
     * 根据对话ID获取列表
     */
    List<FunctionalChatDialogPerson> getByDialogId(Long dialogId);

    /**
     * 根据对话ID获取用户账号ID
     */
    List<Long> getUserAccountIdsByDialogId(Long dialogId);

}
