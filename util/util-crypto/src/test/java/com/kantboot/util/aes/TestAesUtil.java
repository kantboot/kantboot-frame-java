package com.kantboot.util.aes;

import com.kantboot.util.crypto.aes.AesUtil;
import org.junit.Test;

public class TestAesUtil {

    @Test
    public void testEncryptAndDecrypt() {
        String content = "测试AES加密解密";
        System.out.println("原文：" + content);

        // 加密
        String encryptedContent = AesUtil.encrypt(content,"1234567890123456");
        System.out.println("加密后：" + encryptedContent);

        // 解密
        String decryptedContent = AesUtil.decrypt(encryptedContent,"1234567890123456");
        System.out.println("解密后：" + decryptedContent);
    }

}
