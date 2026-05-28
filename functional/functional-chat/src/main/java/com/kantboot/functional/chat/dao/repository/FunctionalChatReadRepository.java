package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatRead;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionalChatReadRepository
    extends JpaRepository<FunctionalChatRead,Long> {

    /**
     * 根据用户账号ID和是否已读获取数量
     */
    Long countByUserAccountIdAndRead(Long userAccountId, Boolean read);

    /**
     * 根据用户账号ID、对话ID和是否已读获取数量
     */
    Long countByUserAccountIdAndDialogIdAndRead(
        Long userAccountId,
        Long dialogId,
        Boolean read);


    /**
     * 根据对话ID、用户账号ID和是否已读获取列表
     * @param dialogId
     * @param userAccountId
     * @param read
     * @return
     */
    List<FunctionalChatRead> findByDialogIdAndUserAccountIdAndRead(
        Long dialogId,
        Long userAccountId,
        Boolean read
    );


    @Transactional
    @Modifying
    @Query("""
        UPDATE FunctionalChatRead t
        SET t.read = true
        WHERE t.dialogId = :dialogId
          AND t.userAccountId = :userAccountId
        """)
    void updateReadByDialogIdAndUserAccountId(Long dialogId, Long userAccountId);

}
