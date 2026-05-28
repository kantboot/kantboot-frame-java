package com.kantboot.fp.comment.repository;

import com.kantboot.fp.comment.domain.entity.FpComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FpCommentRepository
    extends JpaRepository<FpComment,Long> {

    /**
     * 根据code前缀查询
     */
    @Query("""
    SELECT fpc FROM FpComment fpc
                WHERE (:#{#params?.code} IS NULL OR :#{#params?.code} = '' OR fpc.code LIKE %:#{#params?.code}%) AND
                      (:#{#params?.typeCode} IS NULL OR :#{#params?.typeCode} = '' OR fpc.typeCode LIKE %:#{#params?.typeCode}%) AND
                      (:#{#params?.maxId} IS NULL OR fpc.id <= :#{#params?.maxId}) AND
                      (:#{#params?.minId} IS NULL OR fpc.id >= :#{#params?.minId}) AND (:#{#params?.auditStatus} IS NULL OR :#{#params?.auditStatus} = '' OR fpc.auditStatus = :#{#params?.auditStatus}) AND
                      (:#{#params?.id} IS NULL OR fpc.id = :#{#params?.id}) AND fpc.isDelete = false
          """)
    Page<FpComment> getBodyData(
            @Param("params") FpComment params,
            Pageable pageable);

}
