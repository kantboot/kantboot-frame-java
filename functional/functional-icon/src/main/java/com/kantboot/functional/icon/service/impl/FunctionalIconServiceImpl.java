package com.kantboot.functional.icon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.functional.icon.dao.repository.FunctionalIconRepository;
import com.kantboot.functional.icon.domain.entity.FunctionalIcon;
import com.kantboot.functional.icon.service.IFunctionalIconService;
import com.kantboot.util.base.control.service.IBaseService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunctionalIconServiceImpl
    implements IFunctionalIconService {

    @Resource
    private FunctionalIconRepository repository;

    @Resource
    private IBaseService<FunctionalIcon,Long> baseService;

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    @Override
    public List<FunctionalIcon> getAll() {
        return repository.findAll();
    }

    @Override
    public FunctionalIcon getByCode(String code) {
        // 先从缓存中获取
        String s = cacheUtil.get("functionalIcon:" + code);
        if (s != null) {
            return JSON.parseObject(s, FunctionalIcon.class);
        }
        FunctionalIcon byCode = repository.findByCode(code);
        if (byCode != null) {
            // 将结果存入缓存
            cacheUtil.set("functionalIcon:" + code, JSON.toJSONString(byCode));
        }
        return byCode;
    }

    @Override
    public void visitByCode(String code,String color,String strokeWidth) {
        // 直接转给前端svg图片
        FunctionalIcon byCode = getByCode(code);
        if (byCode == null) {
            response.setStatus(404);
            return;
        }
        response.setStatus(200);
        response.setHeader("Cache-Control", "public, max-age=31536000");
        response.setContentType("image/svg+xml");
        String content = byCode.getContent();
//        String tag = """
//                    <svg viewBox="0 0 24 24"
//                         fill="currentColor"
//                         xmlns="http://www.w3.org/2000/svg"
//                         stroke="currentColor"
//                         stroke-width="strokeWidth"
//                         fill
//                    >
//                """+content+"""
//                    </svg>
//                """;
        String tag = "<svg viewBox=\"0 0 24 24\"\n" +
                "     fill=\"currentColor\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "     stroke=\"currentColor\"\n" +
                "     stroke-width=\"" + strokeWidth + "\"\n" +
                ">\n" + content + "\n" +
                "</svg>\n";
        if(color!=null && !color.isEmpty()){
            tag = tag.replace("currentColor", color);
        }
        try {
            response.getWriter().write(tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PageResult getBodyData(PageParam<Map<String,Object>> pageParam) {
        PageParam<Map<String, Object>> pageParamR = BeanUtil.copyProperties(pageParam, PageParam.class);
        Map<String,Object> map = new HashMap<>();
        map.put("code:and:vague", pageParam.getData().get("keyword"));
        pageParamR.setData(map);
        return baseService.getBodyDataEasy(pageParamR, FunctionalIcon.class);
    }

}
