package com.kantboot.test.starter.main;

public class HeapStackExample {
    public static void main(String[] args) {
        // 基本类型 - 值存储在栈中
        int stackVar = 10;
        
        // 对象 - 实例存储在堆中，引用存储在栈中
        Object heapObj = new Object();
        
        // 方法调用 - 创建新的栈帧
        methodCall();
    }
    
    static void methodCall() {
        // 局部变量存储在栈中
        String localVar = "栈上的引用，字符串在堆中";
        System.out.println(localVar);
    }
}