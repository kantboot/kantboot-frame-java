package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalChatUserAccountRelationshipRepository
    extends JpaRepository<FunctionalChatUserAccountRelationship,Long> {

    /**
     * 根据用户账号ID查询关系
     */
    FunctionalChatUserAccountRelationship findFirstByUserAccountId(Long userAccountId);


}
