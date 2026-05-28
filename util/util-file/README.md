# util-file（文件操作工具模块）

文件操作工具模块，提供丰富的文件处理功能，包括文件下载、MD5计算、图片压缩等。

## 功能特性

- 文件类型识别
  - 支持常见文件后缀识别
  - 自动映射MIME类型
  - 扩展的文件类型支持

- 文件下载功能
  - 从网络URL下载文件
  - 本地文件下载
  - 支持大文件分块处理

- 文件校验
  - 文件MD5计算
  - 支持多种输入源(MultipartFile/字节数组/文件路径)

- 图片处理
  - 图片压缩
  - 质量调整
  - 尺寸缩放

- 文件流处理
  - 文件流转MultipartFile
  - 文件流转字节数组
  - 文件流高效处理

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-file</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

```java
// 从URL下载文件
String fileName = FileUtil.netFileDownload("http://example.com/file.jpg", "/downloads");

// 计算文件MD5
String md5 = FileUtil.getMd5("/uploads", "test.jpg");

// 压缩图片
MultipartFile compressedImage = FileUtil.compressImage("/images/original.png", 0.5);

// 获取文件类型
String contentType = FileUtil.getContentType("document.pdf");
```

## API说明

### 文件下载
- `downloadFromUrl(String urlStr, String savePath, String fileName)`: 从URL下载文件到指定路径
- `netFileDownload(String urlAddress, String destinationDir)`: 从网络下载文件并返回文件名
- `createFileItem(String filePath, String fileName)`: 创建本地文件的FileItem对象

### 文件校验
- `getMd5(String filePath, String fileName)`: 计算文件的MD5值
- `getMd5(byte[] bytes)`: 计算字节数组的MD5值
- `getMd5(MultipartFile file)`: 计算MultipartFile的MD5值

### 图片处理
- `compressImage(String filePath, double quality)`: 压缩图片并返回MultipartFile

### 文件类型
- `getSuffix(String fileName)`: 获取文件后缀
- `getContentType(String fileName)`: 获取文件MIME类型

## 配置说明

1. 确保项目中已配置文件上传大小限制(如需要):

```properties
# Spring Boot配置示例
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

2. 如需扩展支持的文件类型，可修改CONTENT_TYPE_MAP_BY_SUFFIX静态映射

## 注意事项

1. 文件操作建议:
   - 大文件操作时注意内存使用
   - 网络下载时添加超时处理
   - 敏感文件操作需添加权限控制

2. 性能考虑:
   - 频繁文件操作建议使用缓存
   - 批量操作考虑使用异步处理
   - 图片压缩根据实际需求调整质量参数

3. 安全建议:
   - 验证文件上传类型
   - 限制文件上传大小
   - 对用户上传文件进行病毒扫描

## 依赖

- Spring Web
- Hutool工具包
- Apache Commons FileUpload
- Lombok

## 版本

${revision}
