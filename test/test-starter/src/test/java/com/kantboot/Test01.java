package com.kantboot;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.repository.SysSettingRepository;
import com.kantboot.test.starter.TestStarterApplication;
import com.kantboot.util.i18n.util.I18nUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

@SpringBootTest(classes = TestStarterApplication.class)
public class Test01 {

    @Resource
    private SysSettingRepository repository;

    @Test
    public void test() {
        String i18nTopKey = I18nUtil.getI18nTopKey(SysSetting.class);
        System.out.println("i18nTopKey = " + i18nTopKey);

        String fieldFromI18nCenterKey = I18nUtil.getFieldFromI18nCenterKey(SysSetting.class);
        System.out.println("fieldFromI18nCenterKey = " + fieldFromI18nCenterKey);

        List<String> fieldsFromI18nBottomKey = I18nUtil.getFieldsFromI18nBottomKey(SysSetting.class);
        System.out.println("fieldsFromI18nBottomKey = " + JSON.toJSONString(fieldsFromI18nBottomKey));

    }

    @Test
    public void test2() {

        // 获取ThreadMXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // 获取所有线程ID
        long[] threadIds = threadMXBean.getAllThreadIds();
        System.out.println("##### 线程堆栈信息 #####");
        System.out.println("线程数: " + threadIds.length);

        // 获取每个线程的详细信息
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            if (threadInfo != null) {
                System.err.println("--------------------");
                // 绿色
                System.out.println("\u001B[32m");

                System.out.println("\n线程ID: " + threadInfo.getThreadId());
                System.out.println("线程名称: " + threadInfo.getThreadName());
                System.out.println("线程状态: " + threadInfo.getThreadState());
                System.out.println("优先级: " + threadInfo.getPriority());
                System.out.println("是否守护线程: " + threadInfo.isDaemon());
                // 打印堆栈跟踪
                System.out.println("堆栈跟踪:");
                for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
                    System.out.println("\t" + stackTraceElement.getClassLoaderName());
                }
                System.err.println(JSON.toJSONString(threadInfo, JSONWriter.Feature.PrettyFormat));
                // 重置颜色
                System.out.println("\u001B[0m");
            }

        }
        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
