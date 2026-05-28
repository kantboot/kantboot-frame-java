package com.kantboot.functional.chat.dao.repository;

import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogDelete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalChatDialogDeleteRepository
    extends JpaRepository<FunctionalChatDialogDelete,Long> {

    /**
     * 根据编码查询
     */
    FunctionalChatDialogDelete findByCode(String code);

    /**
     * 根据编码列表查询
     */
    List<FunctionalChatDialogDelete> findByCodeIn(List<String> codes);

}
