package com.kantboot.util.crypto.rsa;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密工具类
 * @author 方某方
 */
public class RsaUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY_STR = "PublicKeyStr";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY_STR = "PrivateKeyStr";

    /**
     * RSA最大加密明文大小(字节数)
     */
    public static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小(字节数)
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 分隔符
     */
    private static final String SEPARATOR = "&";



    /**
     * 生成公钥私钥
     */
    public static Map<String, String> initKeyPair() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 初始化密钥对生成器，密钥大小为1024
        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyStr = encryptBASE64(publicKey.getEncoded());
        String privateKeyStr = encryptBASE64(privateKey.getEncoded());

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY_STR, publicKeyStr);
        keyMap.put(PRIVATE_KEY_STR, privateKeyStr);
        return keyMap;
    }

    /**
     * 公钥加密
     * @param str 待加密字符串
     * @param key 公钥
     * @return java.lang.String
     */
    @SneakyThrows
    public static String encryptByPublicKey(String str, String key) {
        byte[] data = str.getBytes();
        PublicKey publicKey = strToPublicKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = dataSegment(data, cipher, MAX_ENCRYPT_BLOCK);
        return encryptBASE64(bytes);
    }


    private static String decryptByPrivateKeyFront(String str, String key) throws Exception {
        byte[] data = decryptBASE64(str);
        PrivateKey privateKey = strToPrivateKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = dataSegment(data, cipher, MAX_DECRYPT_BLOCK);
        return new String(bytes);
    }

    /**
     * 私钥解密
     * @param str 待解密字符串
     * @param key 私钥
     * @return java.lang.String
     */
    @SneakyThrows
    public static String decryptByPrivateKey(String str, String key) {
        // 如果字符串中包含分隔符，则说明是分段加密的数据
        if (str.contains(SEPARATOR)) {
            String[] split = str.split(SEPARATOR);
            StringBuilder sb = new StringBuilder();
            for (String s : split) {
                String s1 = decryptByPrivateKeyFront(s, key);
                sb.append(s1);
            }
            return sb.toString();
        }

        return decryptByPrivateKeyFront(str, key);
    }


    /**
     * 私钥加密
     * @param str 待加密字符串
     * @param key 私钥
     * @return java.lang.String
     */
    public static String encryptByPrivateKey(String str, String key) throws Exception {
        byte[] data = str.getBytes();
        PrivateKey privateKey = strToPrivateKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] bytes = dataSegment(data, cipher, MAX_ENCRYPT_BLOCK);
        return encryptBASE64(bytes);
    }


    /**
     * 公钥解密
     * @param str 待解密字符串
     * @param key 公钥
     * @return java.lang.String
     */
    public static String decryptByPublicKey(String str, String key) throws Exception {
        byte[] data = decryptBASE64(str);
        PublicKey publicKey = strToPublicKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] bytes = dataSegment(data, cipher, MAX_DECRYPT_BLOCK);
        return new String(bytes);
    }


    /**
     * 使用私钥对数据进行数字签名
     * @param str 待加签字符串
     * @param privateKey 私钥
     * @return java.lang.String
     */
    public static String sign(String str, String privateKey) throws Exception {
        byte[] data = str.getBytes();
        PrivateKey priKey = strToPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        byte[] bytes = signature.sign();
        return encryptBASE64(bytes);
    }


    /**
     * 使用公钥对数据验证签名
     * @param str 待验签字符串(即要加签的内容)
     * @param publicKey 公钥
     * @param sign 数据签名
     * @return boolean
     */
    public static boolean verify(String str, String publicKey, String sign) throws Exception {
        byte[] data = str.getBytes();
        byte[] signBytes = decryptBASE64(sign);
        PublicKey pubKey = strToPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(signBytes);
    }

    /**
     * 对数据进行分段加密或解密
     * @param data 待加密或解密数据
     * @param cipher Cipher对象
     * @param maxBlock 最大加密或解密长度
     * @return byte[]
     */
    private static byte[] dataSegment(byte[] data, Cipher cipher, int maxBlock) throws Exception{
        byte[] toByteArray;
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        byte[] cache;
        // 对数据分段加密解密
        while (inputLen - offSet > 0) {
            int var = Math.min(inputLen - offSet, maxBlock);
            cache = cipher.doFinal(data, offSet, var);
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        toByteArray = out.toByteArray();
        out.close();
        return toByteArray;
    }


    /**
     * 获取私钥
     * @param str 私钥字符串
     * @return java.security.PrivateKey
     */
    private static PrivateKey strToPrivateKey(String str) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decryptBASE64(str));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 获取公钥
     * @param str 公钥字符串
     * @return java.security.PrivateKey
     */
    private static PublicKey strToPublicKey(String str) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decryptBASE64(str));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(x509KeySpec);
    }


    /**
     * 字节数组转字符串
     * @param bytes 字节数组
     * @return java.lang.String
     */
    private static String encryptBASE64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * 字符串转字节数组
     * @param str 字符串
     * @return byte[]
     */
    private static byte[] decryptBASE64(String str) {
        return Base64.getDecoder().decode(str);
    }


}