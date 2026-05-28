package com.kantboot.functional.file.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Locale;

@Slf4j
public class ImageCompressUtil {

    private ImageCompressUtil() {}

    /**
     * ✅ 唯一入口
     */
    public static MultipartFile compressIfNeeded(MultipartFile file, Long targetBytes) {
        if (file == null) return null;
        if (targetBytes == null || targetBytes <= 0) return file;
        if (file.getSize() <= targetBytes) return file;

        if (!isImage(file)) return file;

        String format = detectFormat(file);
        if ("gif".equals(format)) return file; // 动图不处理

        try {
            BufferedImage src = ImageIO.read(file.getInputStream());
            if (src == null) return file;

            BufferedImage img = toRGB(src);

            if ("png".equals(format)) {
                return compressPng(file, img, targetBytes);
            }

            // jpg / jpeg / 其他图片 → jpg 方式最稳
            return compressJpeg(file, img, targetBytes);

        } catch (Exception e) {
            log.warn("image compress failed, fallback original", e);
            return file;
        }
    }

    // ================= 图片类型判断 =================

    private static boolean isImage(MultipartFile file) {
        String ct = file.getContentType();
        if (ct != null && ct.startsWith("image/")) return true;

        String name = file.getOriginalFilename();
        if (name == null) return false;

        String l = name.toLowerCase(Locale.ROOT);
        return l.endsWith(".jpg") || l.endsWith(".jpeg")
                || l.endsWith(".png") || l.endsWith(".bmp")
                || l.endsWith(".tif") || l.endsWith(".tiff")
                || l.endsWith(".webp");
    }

    private static String detectFormat(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (StrUtil.isNotBlank(name) && name.contains(".")) {
            return name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        }
        String ct = file.getContentType();
        if (ct != null && ct.startsWith("image/")) {
            return ct.substring(6);
        }
        return "unknown";
    }

    // ================= JPEG 压缩 =================

    private static MultipartFile compressJpeg(
            MultipartFile origin,
            BufferedImage image,
            long targetBytes
    ) throws IOException {

        float quality = 0.9f;
        float minQuality = 0.25f;
        double scale = 0.85;

        BufferedImage current = image;
        byte[] data = writeJpeg(current, quality);

        while (data.length > targetBytes) {
            if (quality > minQuality) {
                quality -= 0.12f;
            } else {
                current = resize(current, scale);
            }
            data = writeJpeg(current, quality);
        }

        return new ByteArrayMultipartFile(
                data,
                "file",
                rewriteName(origin.getOriginalFilename(), "jpg"),
                "image/jpeg"
        );
    }

    // ================= PNG 压缩（无损 + 降尺寸） =================

    private static MultipartFile compressPng(
            MultipartFile origin,
            BufferedImage image,
            long targetBytes
    ) throws IOException {

        double scale = 0.85;
        BufferedImage current = image;
        byte[] data = writePng(current);

        while (data.length > targetBytes) {
            current = resize(current, scale);
            data = writePng(current);
        }

        return new ByteArrayMultipartFile(
                data,
                "file",
                rewriteName(origin.getOriginalFilename(), "png"),
                "image/png"
        );
    }

    // ================= ImageIO helpers =================

    private static byte[] writeJpeg(BufferedImage img, float quality) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(img, null, null), param);

        ios.close();
        writer.dispose();
        return out.toByteArray();
    }

    private static byte[] writePng(BufferedImage img) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "png", out);
        return out.toByteArray();
    }

    private static BufferedImage toRGB(BufferedImage src) {
        BufferedImage rgb = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = rgb.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return rgb;
    }

    private static BufferedImage resize(BufferedImage src, double scale) {
        int w = (int) (src.getWidth() * scale);
        int h = (int) (src.getHeight() * scale);
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    private static String rewriteName(String name, String ext) {
        if (StrUtil.isBlank(name)) return "upload." + ext;
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(0, i) + "." + ext : name + "." + ext;
    }
}
