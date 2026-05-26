package com.kantboot.functional.chat.service;

public interface IFunctionalChatReadService {

    /**
     * 标记成未读
     */
    void markAsUnread(Long messageId);

    /**
     * 编辑已读状态
     */
    void markAsRead(Long dialogId,Long userAccountId);

    void markAsReadSelf(Long dialogId);

}
