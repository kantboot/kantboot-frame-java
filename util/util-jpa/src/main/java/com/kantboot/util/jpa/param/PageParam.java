package com.kantboot.util.jpa.param;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 用来请求分页的参数的封装类
 */
@Data
@Accessors(chain = true)
public class PageParam<T> implements Serializable {

    private Integer pageSize;

    private Integer pageNumber;

    private T data;

    /**
     * 排序方式
     */
    private String orderBy;

    /**
     * 排序字段
     */
    private String sort;

    public Pageable getPageable() {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        if (sort == null) {
            return pageRequest;
        } else if (orderBy == null) {
            return pageRequest.withSort(Sort.by(Sort.Order.asc(sort)));
        } else if (orderBy.equalsIgnoreCase("DESC")) {
            return pageRequest.withSort(Sort.by(Sort.Order.desc(sort)));
        }
        return pageRequest.withSort(Sort.by(Sort.Order.asc(sort)));
    }

}
