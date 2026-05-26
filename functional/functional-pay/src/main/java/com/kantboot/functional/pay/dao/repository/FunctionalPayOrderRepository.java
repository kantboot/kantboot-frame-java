package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 支付订单数据访问接口
 * @author FangMoFang
 */
public interface FunctionalPayOrderRepository
    extends JpaRepository<FunctionalPayOrder, Long> {

    /**
     * 根据订单ID和订单状态编码获取订单
     */
    FunctionalPayOrder findByIdAndStatusCode(Long id, String statusCode);

    /**
     * 根据用户账号ID获取支付状态编码的订单列表
     */
    @Query("""
        select o from FunctionalPayOrder o
        where o.userAccountId = ?1
        and o.statusCode = ?2
        order by o.gmtCreate DESC
""")
    List<FunctionalPayOrder> findByUserAccountIdAndStatusCode(Long userAccountId, String statusCode);

    /**
     * 根据IDS查询订单
     */
    List<FunctionalPayOrder> findByIdIn(List<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from FunctionalPayOrder o where o.id = :id")
    Optional<FunctionalPayOrder> findByIdForUpdate(@Param("id") Long id);



}