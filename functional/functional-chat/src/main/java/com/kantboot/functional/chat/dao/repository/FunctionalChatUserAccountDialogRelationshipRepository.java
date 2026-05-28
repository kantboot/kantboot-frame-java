package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatUserAccountDialogRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionalChatUserAccountDialogRelationshipRepository
    extends JpaRepository<FunctionalChatUserAccountDialogRelationship,Long> {

    /**
     * 根据用户账号ID和对话ID查询关系
     */
    FunctionalChatUserAccountDialogRelationship findFirstByUserAccountIdAndDialogId(
        Long userAccountId,
        Long dialogId
    );

    /**
     * 根据编码列表查询关系
     */
    @Query("""
        SELECT r FROM FunctionalChatUserAccountDialogRelationship r
        WHERE r.code IN :codes
    """)
    List<FunctionalChatUserAccountDialogRelationship> findFirstByCodeIn(
        List<String> codes
    );

}
