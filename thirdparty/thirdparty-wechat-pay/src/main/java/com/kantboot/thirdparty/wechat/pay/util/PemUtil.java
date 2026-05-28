package com.kantboot.thirdparty.wechat.pay.util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * pem文件的工具类
 * 用于将pem文件转换为私钥
 * @author 方某方
 */
@Slf4j
public class PemUtil implements Serializable {

    @SneakyThrows
    public static String getPrivateKey(String pemUrl) {
        URL url = new URL(pemUrl);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        byte[] keyBytes = outputStream.toByteArray();

        return new String(keyBytes, StandardCharsets.UTF_8);
    }

    /**
     * 获取base64的签名
     * @return base64的签名
     */
    public static String getBase64Signature(String data,String privateKeyPem) {

        try {
            byte[] privateKeyBytes = parsePrivateKey(privateKeyPem);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static byte[] parsePrivateKey(String privateKeyPem) throws IOException {
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(privateKeyPem);
    }
}
