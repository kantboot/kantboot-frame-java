package com.kantboot.fp.carousel.service.impl;

import com.kantboot.fp.carousel.domain.entity.FpCarousel;
import com.kantboot.fp.carousel.dao.repository.FpCarouselRepository;
import com.kantboot.fp.carousel.service.IFpCarouselService;
import com.kantboot.util.jpa.param.PageParam;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FpCarouseServiceImpl
        implements IFpCarouselService {

    @Resource
    private FpCarouselRepository repository;

    @Override
    public FpCarousel save(FpCarousel fpCarousel) {
        boolean flag = fpCarousel.getId() == null;
        FpCarousel save = repository.save(fpCarousel);
        if (flag) {
            save.setSort(save.getId());
        }
        if (fpCarousel.getSort() == null) {
            save.setSort(save.getId());
        }
        save = repository.save(save);
        return save;
    }

    @Override
    public FpCarousel moveUp(FpCarousel fpCarousel) {

        // 根据ID获取当前轮播图
        FpCarousel findById = repository.findById(fpCarousel.getId()).orElse(null);
        Long sort = findById.getSort();
        // 获取比当前轮播图sort小的第一个轮播图
        PageParam<FpCarousel> pageParam = new PageParam<>();
        pageParam.setPageSize(1);
        pageParam.setPageNumber(1);
        Page<FpCarousel> bySortAsc = repository.findBySortDesc(sort, pageParam.getPageable());
        FpCarousel other = bySortAsc.getContent().stream().findFirst().orElse(null);
        // 更换sort
        if (other != null) {
            Long temp = other.getSort();
            other.setSort(findById.getSort());
            findById.setSort(temp);
            repository.save(other);
            repository.save(findById);
        }
        return findById;
    }

    @Override
    public FpCarousel moveDown(FpCarousel fpCarousel) {

        // 根据ID获取当前轮播图
        FpCarousel findById = repository.findById(fpCarousel.getId()).orElse(null);
        Long sort = findById.getSort();
        // 获取比当前轮播图sort大的第一个轮播图
        PageParam<FpCarousel> pageParam = new PageParam<>();
        pageParam.setPageSize(1);
        pageParam.setPageNumber(1);
        Page<FpCarousel> bySortAsc = repository.findBySortAsc(sort, pageParam.getPageable());
        FpCarousel other = bySortAsc.getContent().stream().findFirst().orElse(null);
        // 更换sort
        if (other != null) {
            Long temp = other.getSort();
            other.setSort(findById.getSort());
            findById.setSort(temp);
            repository.save(other);
            repository.save(findById);
        }
        return findById;
    }

    @Override
    public List<FpCarousel> getByTypeCode(String typeCode) {
        return repository.findByTypeCode(typeCode);
    }

    @Override
    public FpCarousel getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
