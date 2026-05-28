# util-qrcode（QRCode工具模块）

QRCode工具模块，提供QRCode生成和解析功能，支持自定义尺寸、颜色、logo等参数。

## 功能特性

- **QRCode生成**：支持文本、URL等内容生成QRCode
- **QRCode解析**：从图片中解析QRCode内容
- **自定义参数**：支持设置尺寸、颜色、边距等
- **Logo支持**：可在QRCode中心添加Logo图片
- **多种输出格式**：支持PNG、JPEG、SVG等格式

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-qrcode</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

1. **生成QRCode**：
```java
// 生成默认QRCode
BufferedImage qrCode = QRCodeUtil.generate("https://kantboot.com");

// 保存到文件
QRCodeUtil.saveToFile(qrCode, "qrcode.png", "PNG");
```

2. **生成带Logo的QRCode**：
```java
BufferedImage logo = ImageIO.read(new File("logo.png"));
BufferedImage qrCode = QRCodeUtil.generateWithLogo(
    "https://kantboot.com", 
    logo,
    200,  // 宽度
    200,  // 高度
    40    // Logo大小
);
```

3. **解析QRCode**：
```java
String content = QRCodeUtil.decode(new File("qrcode.png"));
System.out.println("解析结果: " + content);
```

4. **自定义参数**：
```java
QRCodeConfig config = new QRCodeConfig()
    .setSize(300)          // 尺寸
    .setMargin(2)          // 边距
    .setForegroundColor(Color.BLACK)  // 前景色
    .setBackgroundColor(Color.WHITE); // 背景色

BufferedImage qrCode = QRCodeUtil.generate("Custom QRCode", config);
```

## API说明

### 核心类

#### QRCodeUtil
- **功能**：QRCode生成和解析工具类
- **主要方法**：
  - `generate(String content)`: 生成默认QRCode
  - `generate(String content, QRCodeConfig config)`: 根据配置生成QRCode
  - `generateWithLogo(String content, BufferedImage logo, int width, int height, int logoSize)`: 生成带Logo的QRCode
  - `decode(File qrCodeFile)`: 解析QRCode内容
  - `saveToFile(BufferedImage qrCode, String filePath, String format)`: 保存QRCode到文件

#### QRCodeConfig
- **功能**：QRCode配置类
- **配置项**：
  - `size`: QRCode尺寸
  - `margin`: 边距
  - `foregroundColor`: 前景色
  - `backgroundColor`: 背景色
  - `errorCorrectionLevel`: 容错级别(L/M/Q/H)
  - `charset`: 字符编码

## 配置说明

1. **默认配置**：
```java
// 可通过修改QRCodeConfig的静态默认值来全局配置
QRCodeConfig.DEFAULT_SIZE = 300;
QRCodeConfig.DEFAULT_MARGIN = 1;
QRCodeConfig.DEFAULT_ERROR_CORRECTION = ErrorCorrectionLevel.H;
```

2. **日志配置**：
```properties
# 如需调试可开启DEBUG日志
logging.level.com.kantboot.util.qrcode=DEBUG
```

## 最佳实践

1. **尺寸选择**：
   - 小尺寸(100-200px): 适用于网页
   - 中尺寸(200-300px): 适用于打印材料
   - 大尺寸(300+px): 适用于远距离扫描

2. **容错级别**：
   - L(7%): 最小容错，最高密度
   - M(15%): 推荐默认值
   - Q(25%): 中等容错
   - H(30%): 最高容错，适合带Logo或可能受损的场景

3. **Logo使用**：
   - Logo大小建议不超过QRCode尺寸的20%
   - 使用高对比度的Logo
   - 避免复杂图案影响扫描

## 依赖

- ZXing Core (QRCode核心库)
- ZXing JavaSE (扩展支持)
- Java AWT/Swing (图形处理)

## 版本

${revision}
