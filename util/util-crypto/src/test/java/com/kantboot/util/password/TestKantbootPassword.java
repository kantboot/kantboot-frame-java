package com.kantboot.util.password;

import com.kantboot.util.crypto.password.impl.KantbootPassword;
import org.junit.Test;

public class TestKantbootPassword {

    @Test
    public void testEncrypt(){
        KantbootPassword kantbootPassword = new KantbootPassword();
        kantbootPassword.encrypt("123456");
        // 输出加密后的密码: {kantboot}82bcffe870524928b29ab6366c4084c4.5f53322597d9c9dfd44684933bdca26a
        System.out.println("加密后的密码：" + kantbootPassword.encrypt("123456"));
    }

    @Test
    public void testMatches(){
        KantbootPassword kantbootPassword = new KantbootPassword();
        kantbootPassword.matches("123456",
                "{kantboot}82bcffe870524928b29ab6366c4084c4.5f53322597d9c9dfd44684933bdca26a");
        // 输出是否匹配: true
        System.out.println("是否匹配：" + kantbootPassword.matches("123456",
                "{kantboot}82bcffe870524928b29ab6366c4084c4.5f53322597d9c9dfd44684933bdca26a"));
        // 失败示例: false
        System.out.println("是否匹配：" + kantbootPassword.matches("1234567",
                "{kantboot}82bcffe870524928b29ab6366c4084c4.5f53322597d9c9dfd44684933bdca26b"));
    }

}
