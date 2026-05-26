package com.kantboot.util.rsa;

import com.kantboot.util.crypto.rsa.RsaUtil;
import org.junit.Test;

import java.util.Map;

public class TestRASUtil {

    @Test
    public void testInitKeyPair() {
        Map<String, String> stringStringMap = RsaUtil.initKeyPair();
        String publicKey = stringStringMap.get(RsaUtil.PUBLIC_KEY_STR);
        String privateKey = stringStringMap.get(RsaUtil.PRIVATE_KEY_STR);
        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);
    }

    @Test
    public void testEncryptAndDecrypt() {
        Map<String, String> stringStringMap = RsaUtil.initKeyPair();
        String publicKey = stringStringMap.get(RsaUtil.PUBLIC_KEY_STR);
        String privateKey = stringStringMap.get(RsaUtil.PRIVATE_KEY_STR);

        String content = "测试RSA加密解密";
        System.out.println("原文：" + content);

        // 加密
        String encryptedContent = RsaUtil.encryptByPublicKey(content, publicKey);
        System.out.println("加密后：" + encryptedContent);

        // 解密
        String decryptedContent = RsaUtil.decryptByPrivateKey(encryptedContent, privateKey);
        System.out.println("解密后：" + decryptedContent);
    }

}
