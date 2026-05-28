package com.kantboot.util.log;

import com.kantboot.util.log.domain.LoggerItem;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Logger {

    private List<LoggerItem> loggerItems = new ArrayList<>();

    public void callback(LoggerItem loggerItem){}

    /**
     * 输出日志
     */
    public void printLog(LoggerItem loggerItem) {
        String logMessage = String.format("[%s] [%s] [%s:%d] %s",
                loggerItem.getLevel(),
                loggerItem.getThreadName(),
                loggerItem.getClassName(),
                loggerItem.getLineNumber(),
                loggerItem.getMessage());

        // 获取当前时间的字符串表示
        Date date = new Date();
        // 格式化时间字符串为YYYY-MM-DD HH:MM:SS UTF+8
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        switch (loggerItem.getLevel()) {
            case "INFO" -> System.out.println("KANTBOOT LOG "+formattedDate+" \u001B[32m" + logMessage + "\u001B[0m");
            case "ERROR" -> System.err.println("KANTBOOT LOG "+formattedDate+" \u001B[31m" + logMessage + "\u001B[0m");
            case "WARN" -> System.out.println("KANTBOOT LOG "+formattedDate+" \u001B[33m" + logMessage + "\u001B[0m");
            case "DEBUG" -> System.out.println("KANTBOOT LOG "+formattedDate+" \u001B[34m" + logMessage + "\u001B[0m");
            case null, default -> System.out.println("KANTBOOT LOG "+formattedDate+" "+logMessage);
        }
    }

    public void addItem(LoggerItem loggerItem) {
        loggerItem.setThreadName(Thread.currentThread().getName());
        // 获取当前线程的堆栈信息
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // 找到调用该方法的堆栈信息，一般是第3个
        StackTraceElement caller = null;
        if (stackTrace.length > 0) {
            caller = stackTrace[stackTrace.length - 1];
            loggerItem.setClassName(caller.getClassName());
            loggerItem.setMethodName(caller.getMethodName());
            loggerItem.setFileName(caller.getFileName());
            loggerItem.setLineNumber(caller.getLineNumber());
        }

        Thread.startVirtualThread(() -> {
            String message = loggerItem.getMessageTemplate();
            Object[] params = loggerItem.getParams();
            if (params != null) {
                for (Object param : params) {
                    message = message.replaceFirst("\\{\\}", param == null ? "null" : param.toString());
                }
            }
            loggerItem.setMessage(message);
            printLog(loggerItem);
            loggerItems.add(loggerItem);
            callback(loggerItem);
        });
    }


    public List<LoggerItem> getLoggerItems() {
        return loggerItems;
    }

    public void info(String messageTemplate, Object... params) {
        LoggerItem loggerItem = new LoggerItem();
        loggerItem.setLevel("INFO");
        loggerItem.setMessageTemplate(messageTemplate);
        loggerItem.setParams(params);
        addItem(loggerItem);
    }

    public void error(String messageTemplate, Object... params) {
        LoggerItem loggerItem = new LoggerItem();
        loggerItem.setLevel("ERROR");
        loggerItem.setMessageTemplate(messageTemplate);
        loggerItem.setParams(params);
        addItem(loggerItem);
    }

    public void warn(String messageTemplate, Object... params) {
        LoggerItem loggerItem = new LoggerItem();
        loggerItem.setLevel("WARN");
        loggerItem.setMessageTemplate(messageTemplate);
        loggerItem.setParams(params);
        addItem(loggerItem);
    }

    public void debug(String messageTemplate, Object... params) {
        LoggerItem loggerItem = new LoggerItem();
        loggerItem.setLevel("DEBUG");
        loggerItem.setMessageTemplate(messageTemplate);
        loggerItem.setParams(params);
        addItem(loggerItem);
    }

    @SneakyThrows
    public static void main(String[] args) {
        Logger logger = new Logger(){
            @Override
            public void callback(LoggerItem loggerItem) {
                System.err.println("Callback: "+loggerItem);
            }
        };
        for (int i = 0; i < 100; i++) {
            // 开始时间
            long start = System.nanoTime();
            logger.info("This is an info message with parameters: {}, {}, for: {}", "param1", 123,i);
            // 结束时间
            long end = System.nanoTime();
            System.out.println("Time taken: " + ((end - start)/1_000_000.0) + " ms");
        }
        Thread.sleep(3000);
    }

}
