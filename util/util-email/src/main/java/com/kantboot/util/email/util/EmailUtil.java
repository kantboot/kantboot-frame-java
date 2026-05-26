package com.kantboot.util.email.util;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.kantboot.util.email.domain.dto.EmailAccountDTO;
import com.kantboot.util.email.domain.dto.EmailMonitorDTO;
import com.kantboot.util.email.domain.dto.EmailSendDTO;
import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.io.IOException;
import java.util.Properties;

public class EmailUtil {

    private EmailAccountDTO emailAccountDTO;

    public EmailUtil(EmailAccountDTO emailAccountDTO) {
        this.emailAccountDTO = emailAccountDTO;
    }

    public void send(EmailSendDTO mailSendDTO){
        MailAccount account = new MailAccount();
        account.setHost(emailAccountDTO.getHost());
        account.setFrom(emailAccountDTO.getEmail());
        account.setUser(emailAccountDTO.getUsername());
        account.setPass(emailAccountDTO.getPassword());
        account.setAuth(emailAccountDTO.getAuth());
        account.setSslEnable(emailAccountDTO.getSslEnable());
        account.setPort(mailSendDTO.getPort());
        MailUtil.send(account, mailSendDTO.getTo(), mailSendDTO.getSubject(), mailSendDTO.getContent(), mailSendDTO.getIsHtml());
    }

    // TODO
    public void monitor(EmailMonitorDTO emailMonitorDTO) {
        Integer port = emailMonitorDTO.getPort();
        String username = emailAccountDTO.getUsername();
        String password = emailAccountDTO.getPassword();
        String host = emailAccountDTO.getHost();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", ""+port);
        props.setProperty("mail.imap.ssl.enable", emailMonitorDTO.getSslEnable().toString());
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
                                System.out.println("内容: " + extractContent(message));
                        } catch (MessagingException ex) {
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent e) {
                    // 邮件删除时触发
                }
            });

            while (true) {
                ((IMAPFolder) inbox).idle(); // 阻塞直到新邮件到达
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归提取邮件内容（支持多部分邮件和附件）
     */
    private String extractContent(Part part) throws Exception {
        StringBuilder content = new StringBuilder();

        if (part.isMimeType("text/*")) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);

                // 处理附件
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    String fileName = bodyPart.getFileName();
                    content.append("\n[附件] ").append(fileName);
                    // 实际使用时这里应该保存附件到磁盘
                    // saveAttachment(bodyPart);
                } else {
                    // 递归处理内容部分
                    content.append(extractContent(bodyPart));
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            content.append(extractContent((Part) part.getContent()));
        }
        return content.toString();
    }


}
