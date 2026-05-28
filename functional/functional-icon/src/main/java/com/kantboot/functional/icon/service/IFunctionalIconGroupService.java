package com.kantboot.functional.icon.service;

import com.kantboot.functional.icon.domain.entity.FunctionalIconGroup;

import java.util.List;

public interface IFunctionalIconGroupService {

    /**
     * 获取所有图标组
     */
    List<FunctionalIconGroup> getAll();

}
