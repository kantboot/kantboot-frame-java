package com.kantboot.util.jpa.result;

import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * 返回分页结果的封装类
 */
@Data
public class PageResult {

    Long totalElements;
    Integer totalPage;
    Object content;
    Integer number;
    Integer size;

    public PageResult() {
    }

    public PageResult(Long totalElements, Integer totalPage, Object content, Integer number, Integer size) {
        this.totalElements = totalElements;
        this.totalPage = totalPage;
        this.content = content;
        this.number = number;
        this.size = size;
    }

    public PageResult(Page page) {
        this.totalElements = page.getTotalElements();
        this.totalPage = page.getTotalPages();
        this.content = page.getContent();
        this.number = page.getNumber() + 1;
        this.size = page.getSize();
    }

    public static PageResult of(Page page) {
        return new PageResult(page);
    }

}
