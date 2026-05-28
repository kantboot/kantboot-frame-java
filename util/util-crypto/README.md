# util-crypto（加密工具模块）

加密工具模块，提供AES、RSA加密和密码处理功能。

## 功能特性

- AES加密/解密
  - 支持128/192/256位密钥
  - 支持CBC/ECB/GCM模式
  - 自动处理IV(初始化向量)
  - 支持Base64编码输出

- RSA加密/解密
  - 支持密钥对生成
  - 支持公钥加密/私钥解密
  - 支持私钥签名/公钥验签
  - 支持PKCS#1和PKCS#8格式密钥

- 密码处理
  - 基于BCrypt的密码哈希
  - 密码验证
  - 密码强度校验

## 快速开始

### 引入Maven依赖

```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-crypto</artifactId>
    <version>${revision}</version>
</dependency>
```

### AES加密示例

```java
// 加密
String encrypted = AesUtil.encrypt("plaintext", "secretKey");

// 解密 
String decrypted = AesUtil.decrypt(encrypted, "secretKey");
```

### RSA加密示例

```java
// 生成密钥对
Map<String, String> keyPair = RSAUtil.generateKeyPair(2048);
String publicKey = keyPair.get("publicKey");
String privateKey = keyPair.get("privateKey");

// 公钥加密
String encrypted = RSAUtil.encrypt("plaintext", publicKey);

// 私钥解密
String decrypted = RSAUtil.decrypt(encrypted, privateKey);
```

### 密码处理示例

```java
@Autowired
private IBasePassword passwordUtil;

// 密码哈希
String hashedPassword = passwordUtil.encode("rawPassword");

// 密码验证
boolean matches = passwordUtil.matches("rawPassword", hashedPassword);
```

## API说明

### AesUtil

- `encrypt(String plaintext, String key)`: AES加密
- `decrypt(String ciphertext, String key)`: AES解密
- `encryptToBase64(String plaintext, String key)`: AES加密并返回Base64
- `decryptFromBase64(String base64Ciphertext, String key)`: 解密Base64编码的AES密文

### RSAUtil

- `generateKeyPair(int keySize)`: 生成RSA密钥对
- `encrypt(String plaintext, String publicKey)`: 公钥加密
- `decrypt(String ciphertext, String privateKey)`: 私钥解密
- `sign(String content, String privateKey)`: 私钥签名
- `verify(String content, String sign, String publicKey)`: 公钥验签

### IBasePassword/KantbootPassword

- `encode(CharSequence rawPassword)`: 密码哈希
- `matches(CharSequence rawPassword, String encodedPassword)`: 密码验证
- `isValid(String password)`: 密码强度校验

## 安全注意事项

1. AES使用建议:
   - 优先使用256位密钥
   - 优先使用CBC或GCM模式
   - 不要硬编码密钥，应从安全配置读取

2. RSA使用建议:
   - 密钥长度至少2048位
   - 定期更换密钥对
   - 私钥必须妥善保管

3. 密码处理建议:
   - 不要使用弱密码
   - 密码应包含大小写字母、数字和特殊字符
   - 建议密码长度至少12位

## 依赖

- Bouncy Castle
- Spring Security Crypto
- Apache Commons Codec
- Hutool

## 版本

${revision}
