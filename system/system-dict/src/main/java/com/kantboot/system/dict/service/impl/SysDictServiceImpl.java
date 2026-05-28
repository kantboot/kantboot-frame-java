package com.kantboot.system.dict.service.impl;

import com.kantboot.system.dict.dao.repository.SysDictRepository;
import com.kantboot.system.dict.domain.entity.SysDict;
import com.kantboot.system.dict.service.ISysDictService;
import com.kantboot.util.base.control.service.impl.BaseServiceImpl;
import com.kantboot.util.data.change.annotaion.DataChange;
import com.kantboot.util.data.change.constants.DataChangeCommonKeyConsts;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl
    extends BaseServiceImpl<SysDict,Long>
    implements ISysDictService {

    @Resource
    private SysDictRepository repository;

    @Override
    public List<SysDict> getDict(String dictGroupCode) {
        return repository.findByGroupCode(dictGroupCode);
    }

    @Override
    @DataChange(key = DataChangeCommonKeyConsts.CLIENT_INIT)
    public SysDict save(SysDict entity) {
        entity.setFullCode(entity.getGroupCode()+"."+entity.getCode());
        return repository.save(entity);
    }

    @Override
    @DataChange(key = DataChangeCommonKeyConsts.CLIENT_INIT)
    public void saveBatch(List<SysDict> entityList) {
        for (SysDict sysDict : entityList) {
            sysDict.setFullCode(sysDict.getGroupCode()+"."+sysDict.getCode());
        }
        repository.saveAll(entityList);
    }
}
