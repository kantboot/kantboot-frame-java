package com.kantboot.functional.chat.service;

import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountRelationship;

public interface IFunctionalChatUserAccountRelationshipService {

    /**
     * 计算关系
     */
    void computeRelationship(Long userAccountId);

    /**
     * 计算关系
     */
    void computeRelationship(Long userAccountId,Long dialogId);


    /**
     * 根据用户账号ID获取关系
     */
    FunctionalChatUserAccountRelationship getByUserAccountId(Long userAccountId);

    /**
     * 获取自身的用户关系
     */
    FunctionalChatUserAccountRelationship getBySelf();

}
