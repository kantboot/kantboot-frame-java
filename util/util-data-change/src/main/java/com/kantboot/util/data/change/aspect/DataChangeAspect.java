package com.kantboot.util.data.change.aspect;

import com.kantboot.util.data.change.annotaion.DataChange;
import com.kantboot.util.data.change.service.IDataChangeService;
import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 数据变化切面类
 */
@Aspect
@Component
public class DataChangeAspect {

    @Resource
    private IDataChangeService dataChangeService;

    /**
     * 数据变化切点
     */
    @Pointcut("@annotation(com.kantboot.util.data.change.annotaion.DataChange)")
    public void dataChange() {
    }

    /**
     * 数据变化后置通知
     * @param annotation 数据变化注解
     */
    @After("dataChange() && @annotation(annotation)")
    public void after(DataChange annotation) throws Throwable {
        dataChangeService.dataChange(annotation.key());
        dataChangeService.dataChanges(annotation.keys());
    }

}