package com.kantboot.functional.icon.service;

import com.kantboot.functional.icon.domain.entity.FunctionalIcon;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;

import java.util.List;
import java.util.Map;

public interface IFunctionalIconService {

    List<FunctionalIcon> getAll();

    FunctionalIcon getByCode(String code);

    void visitByCode(String code,String color,String strokeWidth);

    PageResult getBodyData(PageParam<Map<String, Object>> pageParam);

}
