package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalChatDialogPersonRepository
    extends JpaRepository<FunctionalChatDialogPerson,Long> {

    /**
     * 根据对话ID获取
     */
    List<FunctionalChatDialogPerson> findByDialogId(Long dialogId);

}
