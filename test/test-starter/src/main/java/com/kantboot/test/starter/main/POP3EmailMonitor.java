package com.kantboot.test.starter.main;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class POP3EmailMonitor {

    // 配置参数（根据您的邮箱设置修改）
    private static final String POP3_HOST = "mail.kantboot.com"; // POP3服务器地址
    private static final int POP3_PORT = 995; // POP3端口（通常995用于SSL）
    private static final String USERNAME = "login@kantboot.com";
    private static final String PASSWORD = "!Wsfzy123123123";
    private static final boolean USE_SSL = true;
    
    // 监控参数
    private static final int CHECK_INTERVAL = 30; // 检查间隔（秒）
    private static final int MAX_EMAILS_PER_CHECK = 10; // 每次检查最多处理邮件数
    
    // 已处理邮件UID缓存（防止重复处理）
    private static final Set<String> processedUIDs = Collections.synchronizedSet(new HashSet<>());
    private static final AtomicBoolean monitoring = new AtomicBoolean(true);
    
    // 日期格式
    private static final SimpleDateFormat dateFormat = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("POP3 邮件监控系统 - 控制台版");
        System.out.println("开始时间: " + dateFormat.format(new Date()));
        System.out.println("监控邮箱: " + USERNAME);
        System.out.println("按 Ctrl+C 停止监控");
        
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            monitoring.set(false);
            System.out.println("\n监控已停止");
        }));

        // 开始监控
        startMonitoring();
    }

    private static void startMonitoring() {
        int checkCount = 0;
        
        while (monitoring.get()) {
            checkCount++;
            System.out.println("\n--- 检查 #" + checkCount + " [" + dateFormat.format(new Date()) + "] ---");
            
            try {
                checkEmails();
            } catch (Exception e) {
                System.err.println("检查错误: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 等待下一次检查
            try {
                for (int i = 0; i < CHECK_INTERVAL && monitoring.get(); i++) {
                    Thread.sleep(1000);
                    if (i % 5 == 0) System.out.print(".");
                }
                System.out.println();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void checkEmails() throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.store.protocol", "pop3");
        props.put("mail.pop3.host", POP3_HOST);
        props.put("mail.pop3.port", POP3_PORT);
        props.put("mail.pop3.ssl.enable", USE_SSL);
        props.put("mail.pop3.connectiontimeout", "10000");
        props.put("mail.pop3.timeout", "15000");
        
        // 信任所有证书（仅用于测试环境）
        props.put("mail.pop3.ssl.trust", "*");
        props.put("mail.pop3.ssl.checkserveridentity", "false");
        
        Session session = Session.getInstance(props);
        Store store = null;
        
        try {
            // 连接到POP3服务器
            store = session.getStore();
            store.connect(USERNAME, PASSWORD);
            
            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            
            int totalMessages = inbox.getMessageCount();
            int newMessages = 0;
            
            System.out.println("邮件总数: " + totalMessages);
            
            // 从最新的邮件开始检查
            int start = Math.max(1, totalMessages - MAX_EMAILS_PER_CHECK + 1);
            int end = totalMessages;
            
            // 检查新邮件
            for (int i = end; i >= start; i--) {
                Message message = inbox.getMessage(i);
                String uid = generateUID(message);
                
                if (!processedUIDs.contains(uid)) {
                    // 处理新邮件
                    processNewEmail(message);
                    processedUIDs.add(uid);
                    newMessages++;
                }
            }
            
            System.out.println("发现新邮件: " + newMessages + " 封");
            
            // 关闭连接
            inbox.close(false);
        } catch (AuthenticationFailedException e) {
            System.err.println("认证失败: " + e.getMessage());
            System.err.println("请检查用户名和密码是否正确");
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
        }
    }

    private static void processNewEmail(Message message) throws MessagingException, IOException {
        // 获取邮件基本信息
        String from = "未知发件人";
        if (message.getFrom() != null && message.getFrom().length > 0) {
            from = message.getFrom()[0].toString();
        }
        
        String subject = message.getSubject();
        if (subject == null) subject = "(无主题)";
        
        String date = message.getSentDate() != null ? 
                     dateFormat.format(message.getSentDate()) : "未知日期";
        
        // 打印邮件基本信息
        System.out.println("\n==========================================");
        System.out.println("新邮件到达!");
        System.out.println("主题: " + subject);
        System.out.println("发件人: " + from);
        System.out.println("时间: " + date);
        
        // 获取邮件内容
        Object content = message.getContent();
        String textContent = "";
        
        if (content instanceof String) {
            textContent = (String) content;
        } else if (content instanceof MimeMultipart) {
            textContent = extractTextFromMultipart((MimeMultipart) content);
        }
        
        // 打印前200个字符
        int previewLength = Math.min(200, textContent.length());
        String preview = textContent.substring(0, previewLength).replace("\n", " ");
        if (textContent.length() > previewLength) preview += "...";
        
        System.out.println("内容预览: " + preview);
        
        // 保存附件（如果有）
        if (message instanceof MimeMessage) {
            saveAttachments((MimeMessage) message);
        }
        
        System.out.println("==========================================");
    }
    
    private static String extractTextFromMultipart(MimeMultipart multipart)
            throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } 
            else if (bodyPart.isMimeType("text/html")) {
                String html = bodyPart.getContent().toString();
                // 简单去除HTML标签（实际应用中可能需要更复杂的处理）
                result.append(html.replaceAll("<[^>]+>", " "));
            }
            else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(extractTextFromMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        
        return result.toString();
    }
    
    private static void saveAttachments(MimeMessage message) 
            throws MessagingException, IOException {
        Object content = message.getContent();
        
        if (content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) content;
            
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    String fileName = bodyPart.getFileName();
                    System.out.println("发现附件: " + fileName);
                    
                    // 在实际应用中，这里可以保存附件到文件系统
                    // 示例：只显示附件内容的前100个字节
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bodyPart.getDataHandler().writeTo(baos);
                    byte[] data = baos.toByteArray();
                    
                    int previewSize = Math.min(100, data.length);
                    System.out.println("附件预览 (HEX): " + bytesToHex(data, previewSize));
                }
            }
        }
    }
    
    private static String generateUID(Message message) throws MessagingException {
        // 使用发件人、主题、日期和大小生成唯一标识符
        // 实际应用中应该使用UIDL命令获取唯一ID，但JavaMail对POP3 UIDL支持有限
        String from = message.getFrom() != null && message.getFrom().length > 0 ? 
                     message.getFrom()[0].toString() : "";
        String subject = message.getSubject() != null ? message.getSubject() : "";
        String date = message.getSentDate() != null ? 
                     String.valueOf(message.getSentDate().getTime()) : "";
        
        return from + "|" + subject + "|" + date + "|" + message.getSize();
    }
    
    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(length, bytes.length); i++) {
            sb.append(String.format("%02X ", bytes[i]));
            if (i > 0 && (i + 1) % 16 == 0) sb.append("\n");
        }
        return sb.toString();
    }
}