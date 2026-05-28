package com.kantboot.functional.chat.service.impl;

import com.kantboot.functional.chat.dao.repository.FunctionalChatDialogPersonRepository;
import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogPerson;
import com.kantboot.functional.chat.service.IFunctionalChatDialogPersonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionalChatDialogPersonServiceImpl
    implements IFunctionalChatDialogPersonService {

    @Resource
    private FunctionalChatDialogPersonRepository repository;

    @Override
    public void addUserAccounts(Long dialogId, List<Long> userAccountIds) {
        List<FunctionalChatDialogPerson> persons = new ArrayList<>();
        for (Long userAccountId : userAccountIds) {
            FunctionalChatDialogPerson person = new FunctionalChatDialogPerson();
            person.setDialogId(dialogId);
            person.setUserAccountId(userAccountId);
            persons.add(person);
        }
        repository.saveAll(persons);
    }

    @Override
    public List<FunctionalChatDialogPerson> getByDialogId(Long dialogId) {
        return repository.findByDialogId(dialogId);
    }

    @Override
    public List<Long> getUserAccountIdsByDialogId(Long dialogId) {
        List<FunctionalChatDialogPerson> byDialogId = getByDialogId(dialogId);
        List<Long> userAccountIds = new ArrayList<>();
        for (FunctionalChatDialogPerson person : byDialogId) {
            userAccountIds.add(person.getUserAccountId());
        }
        return userAccountIds;
    }
}
