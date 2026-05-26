package com.kantboot.user.account.dao.repository;

import com.kantboot.user.account.domain.entity.UserAccountThirdparty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountThirdpartyRepository
    extends JpaRepository<UserAccountThirdparty, Long> {

    UserAccountThirdparty findByThirdpartyCodeAndKeyAndValue(
            String thirdPartyCode, String key, String value);

    /**
     * 根据用户账号ID查询
     */
    UserAccountThirdparty findByUserAccountIdAndThirdpartyCodeAndKeyAndValue(Long userAccountId,
                                                                             String thirdPartyCode,
                                                                             String key,
                                                                             String value);

    /**
     * 根据用户账号ID查询
     */
    UserAccountThirdparty findByUserAccountIdAndThirdpartyCodeAndKey(
            Long userAccountId,
            String thirdPartyCode,
            String key);

}
