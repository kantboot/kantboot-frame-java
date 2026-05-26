package com.kantboot.test.starter.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class StackTraceAnalyzer {

    public static void main(String[] args) {
        // 示例方法调用链
        new StackTraceAnalyzer().start();
    }

    public void start() {
        // 创建示例对象
        User user = new User("Alice", 30, "alice@example.com");
        
        // 调用方法链
        processUser(user);
    }

    private void processUser(User user) {
        // 修改用户信息
        modifyUserEmail(user, "new_email@example.com");
        
        // 分析当前堆栈
        analyzeCurrentStackTrace();
    }

    private void modifyUserEmail(User user, String newEmail) {
        try {
            // 使用反射修改字段
            Field emailField = User.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(user, newEmail);
            
            System.out.println("成功修改邮箱为: " + newEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分析当前堆栈跟踪信息
     */
    public void analyzeCurrentStackTrace() {
        System.out.println("\n===== 堆栈跟踪分析 =====");
        
        // 获取当前线程的堆栈跟踪
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        
        // 遍历堆栈元素（跳过前两个：getStackTrace 和 analyzeCurrentStackTrace 方法）
        for (int i = 2; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            System.out.printf("\n[堆栈层级 %d] %s%n", i - 2, element);
            
            try {
                // 获取对应的类
                Class<?> clazz = Class.forName(element.getClassName());
                
                // 获取方法信息
                analyzeMethod(clazz, element.getMethodName());
                
                // 获取字段信息
                if (i == 3) { // 特定层级展示字段信息
                    System.out.println("\n字段信息:");
                    for (Field field : clazz.getDeclaredFields()) {
                        System.out.printf("  - %s %s (修饰符: %s)%n",
                                field.getType().getSimpleName(),
                                field.getName(),
                                Modifier.toString(field.getModifiers()));
                    }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("  无法加载类: " + element.getClassName());
            } catch (NoSuchMethodException e) {
                System.out.println("  方法未找到: " + element.getMethodName());
            }
        }
    }

    /**
     * 分析方法信息
     */
    private void analyzeMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        // 查找匹配的方法
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                System.out.println("  方法详情:");
                System.out.println("    - 返回类型: " + method.getReturnType().getSimpleName());
                
                // 参数类型
                System.out.print("    - 参数: ");
                if (method.getParameterTypes().length == 0) {
                    System.out.println("无");
                } else {
                    for (int i = 0; i < method.getParameterTypes().length; i++) {
                        System.out.printf("%s arg%d", 
                                method.getParameterTypes()[i].getSimpleName(), i);
                        if (i < method.getParameterTypes().length - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                }
                
                // 异常信息
                if (method.getExceptionTypes().length > 0) {
                    System.out.print("    - 抛出异常: ");
                    for (Class<?> ex : method.getExceptionTypes()) {
                        System.out.print(ex.getSimpleName() + " ");
                    }
                    System.out.println();
                }
                
                // 注解信息
                if (method.getAnnotations().length > 0) {
                    System.out.print("    - 注解: ");
                    for (Annotation ann : method.getAnnotations()) {
                        System.out.print(ann.annotationType().getSimpleName() + " ");
                    }
                    System.out.println();
                }
                
                return;
            }
        }
        
        throw new NoSuchMethodException(methodName);
    }

    /**
     * 用户类示例
     */
    static class User {
        private String name;
        private int age;
        private String email;
        
        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
        
        @Deprecated
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        @Override
        public String toString() {
            return String.format("User{name='%s', age=%d, email='%s'}", name, age, email);
        }
    }
}