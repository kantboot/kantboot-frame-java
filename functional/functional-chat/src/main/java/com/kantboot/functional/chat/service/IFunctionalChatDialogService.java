package com.kantboot.functional.chat.service;

import com.kantboot.functional.chat.domain.dto.DialogCreateDTO;
import com.kantboot.functional.chat.domain.dto.DialogSearchDTO;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialog;

import java.util.List;

public interface IFunctionalChatDialogService {

    /**
     * 创建会话
     */
    FunctionalChatDialog create(DialogCreateDTO dto);

    /**
     * 创建一对一会话
     * @return
     */
    FunctionalChatDialog toOneToOne(Long userAccountId);

    List<FunctionalChatDialog> getBy(DialogSearchDTO dto);

    FunctionalChatDialog getById(Long id);

    /**
     * 获取当前用户的会话
     */
    List<FunctionalChatDialog> getBySelf(DialogSearchDTO dto);

    /**
     * 根据当前用户删除对话
     */
    void deleteBySelf(Long dialogId);

}
