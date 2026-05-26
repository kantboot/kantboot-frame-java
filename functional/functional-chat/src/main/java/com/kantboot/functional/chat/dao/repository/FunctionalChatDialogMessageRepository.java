package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionalChatDialogMessageRepository
    extends JpaRepository<FunctionalChatDialogMessage,Long> {

    @Query("""
        FROM FunctionalChatDialogMessage t
        WHERE t.dialogId = :dialogId
        ORDER BY t.gmtCreate ASC 
        """)
    List<FunctionalChatDialogMessage> findByDialogId(Long dialogId);

//    findByDialogIdAndMinId

    @Query("""
        FROM FunctionalChatDialogMessage t
        WHERE t.dialogId = :dialogId
          AND t.id > :minId
        ORDER BY t.gmtCreate ASC 
        """)
    List<FunctionalChatDialogMessage> findByDialogIdAndMinId(
        Long dialogId,
        Long minId
    );

}
