package com.kantboot.util.file;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

/**
 * 文件处理工具类
 */
@Slf4j
public class FileUtil {


    protected static final HashMap<String, String> CONTENT_TYPE_MAP_BY_SUFFIX = new HashMap<>();

    static {
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("png", "image/png");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("gif", "image/gif");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("bmp", "image/bmp");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ico", "image/x-icon");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("svg", "image/svg+xml");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("webp", "image/webp");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("tiff", "image/tiff");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("psd", "image/vnd.adobe.photoshop");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mp4", "video/mp4");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("avi", "video/x-msvideo");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wmv", "video/x-ms-wmv");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("flv", "video/x-flv");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mkv", "video/x-matroska");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mp3", "audio/mpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wav", "audio/x-wav");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ogg", "audio/ogg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wma", "audio/x-ms-wma");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("flac", "audio/flac");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("aac", "audio/aac");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("pdf", "application/pdf");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("doc", "application/msword");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xls", "application/vnd.ms-excel");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ppt", "application/vnd.ms-powerpoint");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("zip", "application/zip");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("rar", "application/x-rar-compressed");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("7z", "application/x-7z-compressed");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("gz", "application/x-gzip");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("bz2", "application/x-bzip2");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("tar", "application/x-tar");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jar", "application/java-archive");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("txt", "text/plain");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("html", "text/html");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xml", "text/xml");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("js", "application/javascript");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("css", "text/css");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("json", "application/json");
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     *                 例如：1.jpg
     *                 返回：image/jpeg
     * @return 文件类型
     */
    public static String getContentType(String fileName) {
        String suffix = getSuffix(fileName).toLowerCase();
        return CONTENT_TYPE_MAP_BY_SUFFIX.get(suffix);
    }

    /**
     * 从网络Url中下载文件
     */
    public static void downloadFromUrl(String urlStr, String savePath, String fileName) throws Exception {
        FileOutputStream fos = null;
        URL url = new URL(urlStr);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();
        BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            boolean mkdirs = saveDir.mkdirs();
            if (!mkdirs) {
                throw new Exception("创建文件夹失败");
            }
        }
        File file = new File(saveDir + File.separator + fileName);
        try {
            fos = new FileOutputStream(file);
        } catch (IOException e) {
            log.error("创建文件失败", e);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        int size = 1024;
        byte[] buf = new byte[size];
        int len;
        while ((len = bis.read(buf, 0, size)) != -1) {
            if (fos != null) {
                fos.write(buf, 0, len);
            }
        }
        if (fos != null) {
            fos.close();
        }
        bis.close();
        httpUrl.disconnect();
    }


    public static MultipartFile downloadFromUrl(String fileUrl) {
        try {
            // Create a temporary file
            Path tempFile = Files.createTempFile("download-", ".tmp");

            // Download from URL
            try (InputStream in = new URL(fileUrl).openStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Convert to MultipartFile
            return new MultipartFile() {
                @Override
                public String getName() {
                    return tempFile.getFileName().toString();
                }

                @Override
                public String getOriginalFilename() {
                    String[] parts = fileUrl.split("/");
                    return parts[parts.length - 1].split("\\?")[0];
                }

                @Override
                public String getContentType() {
                    try {
                        return Files.probeContentType(tempFile);
                    } catch (IOException e) {
                        return "application/octet-stream";
                    }
                }

                @Override
                public boolean isEmpty() {
                    try {
                        return Files.size(tempFile) == 0;
                    } catch (IOException e) {
                        return true;
                    }
                }

                @Override
                public long getSize() {
                    try {
                        return Files.size(tempFile);
                    } catch (IOException e) {
                        return 0;
                    }
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return Files.readAllBytes(tempFile);
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return Files.newInputStream(tempFile);
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {
                    Files.copy(tempFile, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from URL: " + fileUrl, e);
        }
    }

    /**
     * 获取文件的字节数组
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 文件的字节数组
     */
    public static byte[] getBytes(String filePath, String fileName) {
        String fileFieldName = "file";
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fileFieldName, "text/plain", false, fileName);
        Path path = Paths.get(filePath);
        File newFile = path.resolve(fileName).toFile();
        writeBytesToItem(newFile, item);
        return item.get();
    }

    /**
     * 将文件的字节流写入到FileItem中
     *
     * @param file 文件对象
     * @param item FileItem对象
     */
    private static void writeBytesToItem(File file, FileItem item) {
        int bytesRead;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            int len = 8192;
            while ((bytesRead = fis.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("获取文件字节数组失败", e);
        }
    }


    /**
     * 获取文件的MD5值
     *
     * @param filePath 文件地址
     *                 例如：/test
     *                 D:/test
     * @param fileName 文件名
     *                 例如：1.jpg
     * @return MD5值
     */
    public static String getMd5(String filePath, String fileName) {
        return DigestUtils.md5DigestAsHex(getBytes(filePath, fileName));
    }

    /**
     * 获取文件的MD5值
     */
    public static String getMd5(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * 获取文件的MD5值
     */
    public static String getMd5(MultipartFile file) {
        try {
            return DigestUtils.md5DigestAsHex(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 网络文件下载
     *
     * @param urlAddress     文件地址
     * @param destinationDir 文件保存目录
     * @return 文件名，文件名是随机生成的UUID加上后缀
     */
    @SneakyThrows
    public static String netFileDownload(String urlAddress, String destinationDir) {
        String fileName = URLUtil.getPath(urlAddress);
        String uuid = IdUtil.simpleUUID();
        fileName = uuid + fileName.substring(fileName.lastIndexOf("."));
        downloadFromUrl(urlAddress, destinationDir, fileName);

        return fileName;
    }


    /**
     * 本地文件下载
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     * @return 文件流
     */
    public static FileItem createFileItem(String filePath, String fileName) {
        String fieldName = "file";
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", false, fileName);
        File newfile = new File(filePath + "/" + fileName);
        int bytesRead;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(newfile);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 压缩图片
     * @param filePath 文件路径
     * @param quality 压缩质量
     * @return 压缩后的图片
     */
    @SneakyThrows
    public static MultipartFile compressImage(String filePath,double quality) {
        File input = new File(filePath);
        BufferedImage originalImage = ImageIO.read(input);
        int targetWidth = (int) (originalImage.getWidth() * quality);
        int targetHeight = (int) (originalImage.getHeight() * quality);

        // 创建一个压缩后的图像缓冲区
        BufferedImage compressedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = compressedImage.createGraphics();

        // 绘制调整大小后的图像
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        // 写入压缩后的图像到ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(compressedImage, "png", baos);
        baos.flush();

        // 将ByteArrayOutputStream转换为字节数组
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        // 将压缩后的图片字节数组转换为MultipartFile
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return imageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return imageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }

    public static String  toBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 根据路径字符串
     */
    public static String toBase64(String filePath,String contentType) {
        try {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            String base64Data = java.util.Base64.getEncoder().encodeToString(bytes);
            return "data:" + contentType + ";base64," + base64Data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert file to Base64: " + filePath, e);
        }
    }

}
