package com.kantboot.tool.area.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.tool.area.dao.repository.ToolAreaRepository;
import com.kantboot.tool.area.domain.entity.ToolArea;
import com.kantboot.tool.area.service.IToolAreaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ToolAreaServiceImpl
    implements IToolAreaService {

    @Resource
    private ToolAreaRepository repository;

    @Override
    public ToolArea getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public ToolArea getByMinCode(String minCode) {
        return repository.findByMinCode(minCode);
    }

    @Override
    public ToolArea getByAlpha2Code(String alpha2Code) {
        if(StrUtil.isEmpty(alpha2Code)){
            return null;
        }
        return repository.findByAlpha2Code(alpha2Code.toUpperCase());
    }

    @Override
    public ToolArea getByAlpha3Code(String alpha3Code) {
        if(StrUtil.isEmpty(alpha3Code)){
            return null;
        }
        return repository.findByAlpha3Code(alpha3Code.toUpperCase());
    }

    @Override
    public List<ToolArea> getByParentCode(String parentCode) {
        return repository.findByParentCode(parentCode);
    }

    @Override
    public List<ToolArea> getByCodeVague(String codeVague) {
        return repository.findByCodeLike("%" + codeVague + "%");
    }

    @Override
    public List<ToolArea> getByLevel(Integer level) {
        return repository.findByLevel(level);
    }
}
