package com.kantboot.test.starter.main;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.Properties;

public class EmailMonitor {

    public static void main(String[] args) {
        // 邮箱配置（替换为实际值）
        String host = "mail.kantboot.com"; // 例如：imap.qq.com, outlook.office365.com
        String username = "login@kantboot.com";
        String password = "!Wsfzy123123123"; // 注意：使用应用专用密码

        monitorInbox(host, username, password);
    }

    public static void monitorInbox(String host, String username, String password) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.connectiontimeout", "5000");
        props.setProperty("mail.imap.timeout", "5000");

        try {
            // 1. 创建Session并连接
            Session session = Session.getInstance(props);
            Store store = session.getStore();
            store.connect(username, password);

            // 2. 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 3. 添加新邮件监听器
            inbox.addMessageCountListener(new MessageCountListener() {
                @Override
                public void messagesAdded(MessageCountEvent e) {
                    System.out.println("\n=== 收到新邮件 ===");
                    for (Message message : e.getMessages()) {
                        try {
                            System.out.println("主题: " + message.getSubject());
                            System.out.println("发件人: " + message.getFrom()[0]);
                            System.out.println("时间: " + message.getSentDate());
                        } catch (MessagingException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent e) {
                    // 邮件删除时触发
                }
            });

            // 4. 启动IDLE监听
            System.out.println("开始监听邮箱... (按Ctrl+C退出)");
            while (true) {
                ((IMAPFolder) inbox).idle(); // 阻塞直到新邮件到达
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}