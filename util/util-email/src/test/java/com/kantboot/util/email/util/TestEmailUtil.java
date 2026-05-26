package com.kantboot.util.email.util;

import com.kantboot.util.email.domain.dto.EmailAccountDTO;
import com.kantboot.util.email.domain.dto.EmailMonitorDTO;
import com.kantboot.util.email.domain.dto.EmailSendDTO;
import org.junit.Test;

public class TestEmailUtil {


    @Test
    public void testSend() {
        EmailAccountDTO emailAccountDTO = new EmailAccountDTO();
        emailAccountDTO.setHost("mail.kantboot.com");
        emailAccountDTO.setUsername("login@kantboot.com");
        emailAccountDTO.setPassword("!Wsfzy123123123");
        emailAccountDTO.setEmail("login@kantboot.com");
        emailAccountDTO.setAuth(true);
        emailAccountDTO.setSslEnable(true);

        EmailUtil emailSendUtil = new EmailUtil(emailAccountDTO);

        EmailSendDTO emailSendDTO = new EmailSendDTO();
        emailSendDTO.setTo("2453201633@qq.com");
        emailSendDTO.setSubject("测试邮件");
        emailSendDTO.setContent("<h1>这是一个测试邮件内容</h1><p>请忽略此邮件。</p>");
        emailSendDTO.setIsHtml(true);
        emailSendUtil.send(emailSendDTO);

    }

    @Test
    public void testMonitor() {
        EmailAccountDTO emailAccountDTO = new EmailAccountDTO();
        emailAccountDTO.setHost("mail.kantboot.com");
        emailAccountDTO.setUsername("login@kantboot.com");
        emailAccountDTO.setPassword("!Wsfzy123123123");
        emailAccountDTO.setEmail("test@kantboot.com");
        emailAccountDTO.setAuth(true);
        emailAccountDTO.setSslEnable(true);

        EmailUtil emailUtil = new EmailUtil(emailAccountDTO);
        emailUtil.monitor(new EmailMonitorDTO());

    }

}
