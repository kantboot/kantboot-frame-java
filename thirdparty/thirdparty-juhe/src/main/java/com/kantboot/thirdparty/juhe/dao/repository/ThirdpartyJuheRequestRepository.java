package com.kantboot.thirdparty.juhe.dao.repository;

import com.kantboot.thirdparty.juhe.domain.entity.ThirdpartyJuheRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyJuheRequestRepository
    extends JpaRepository<ThirdpartyJuheRequest, Long> {

    /**
     * 根据code查询
     * @param code code
     * @return ThirdpartyJuheRequest
     */
    ThirdpartyJuheRequest findByCode(String code);
}
