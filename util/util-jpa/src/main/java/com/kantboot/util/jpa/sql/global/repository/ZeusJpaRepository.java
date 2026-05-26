package com.kantboot.util.jpa.sql.global.repository;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

@Slf4j
public class ZeusJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> {


    private final JpaEntityInformation<T, ?> jpaEntityInformation;

    @Autowired
    public ZeusJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.jpaEntityInformation = entityInformation;
    }

    /**
     * 覆盖原来实现,不更新null字段
     * @param entity 实体
     * @param <S> 实体类型
     * @return 实体
     */
    @Override
    public <S extends T> S save(@Nullable S entity) {
        ID id = (ID) jpaEntityInformation.getId(entity);
        if (id != null) {
            Optional<T> op = findById(id);
            if (op.isPresent()) {
                T t = op.get();
                BeanUtils.copyPropertiesWithoutNull(entity, t);
                entity = (S) t;
            }
        }
        return super.save(entity);
    }
}