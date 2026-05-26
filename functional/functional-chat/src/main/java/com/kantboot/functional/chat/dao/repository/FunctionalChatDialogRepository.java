package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.dto.DialogSearchDTO;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FunctionalChatDialogRepository
        extends JpaRepository<FunctionalChatDialog, Long> {

    /**
     * 根据用户私聊识别号搜索
     */
    FunctionalChatDialog getByOneToOneIdentifier(String oneToOneUserAccountIdentifier);

    /**
     * 根据用户ID获取所在的会话
     */
    @Query("""
        SELECT t FROM FunctionalChatDialog t
        WHERE (t.userAccountId1OfOneToOne = :#{#param.userAccountId} OR t.userAccountId2OfOneToOne = :#{#param.userAccountId})
        AND (:#{#param.maxId} IS NULL OR t.id < :#{#param.maxId})
        AND (:#{#param.minId} IS NULL OR t.id > :#{#param.minId})
        ORDER BY t.gmtModified DESC
        """
    )
    Page<FunctionalChatDialog> getBy(
            @Param("param") DialogSearchDTO dto, Pageable pageable);

    FunctionalChatDialog findByOneToOneIdentifier(String oneToOneIdentifier);

}
