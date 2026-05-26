package com.kantboot.functional.email.setting;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 邮箱设置
 * 用于获取邮箱账号设置
 * Email settings
 * Used to get mailbox account settings
 */
@Slf4j
@Data
@Component
@SettingGroup(code="functionalEmail", name="邮箱设置", description="邮箱设置")
public class FunctionalEmailSetting {

    @Resource
    private ISysSettingService settingService;

    @Setting(code = "smtpPort",name = "邮箱端口", description = "邮箱端口", defaultValue = "465")
    private String smtpPort;

    @Setting(code = "smtpHost",name = "邮箱主机", description = "邮箱主机")
    private String smtpHost;

    @Setting(code = "smtpEmail",name = "邮箱地址", description = "邮箱地址")
    private String smtpEmail;

    @Setting(code = "smtpUsername",name = "邮箱用户名", description = "邮箱用户名")
    private String smtpUsername;

    @Setting(code = "smtpPassword",name = "邮箱密码", description = "邮箱密码")
    private String smtpPassword;

    private void logCode() {
        log.info(""" 
            Email in System settings:
                "functionalEmail.smtpPort" represents the mailbox port
                "functionalEmail.smtpHost" represents the mailbox host
                "functionalEmail.smtpEmail" represents the mailbox address
                "functionalEmail.smtpUsername" represents the mailbox username
                "functionalEmail.smtpPassword" represents the mailbox password
        """);
    }

    /**
     * 获取邮箱账号设置
     * MailAccount是Hutool封装的邮箱账号设置类，用于发送邮件
     * Get mailbox account settings
     * MailAccount is the mailbox account setting class encapsulated by Hutool, used to send emails
     *
     * @return 邮箱账号配置
     *         Mailbox account configuration
     */
    public MailAccount getMailAccount() {
        String functionalEmailSmtpPort = settingService.getValueNoThrow("functionalEmail.smtpPort");
        String functionalEmailSmtpHost = settingService.getValueNoThrow("functionalEmail.smtpHost");
        String functionalEmailSmtpEmail = settingService.getValueNoThrow("functionalEmail.smtpEmail");
        String functionalEmailSmtpUsername = settingService.getValueNoThrow("functionalEmail.smtpUsername");
        String functionalEmailSmtpPassword = settingService.getValueNoThrow("functionalEmail.smtpPassword");
        if(StrUtil.isEmpty(functionalEmailSmtpPort)) {
            // 在系统配置中找不到邮箱端口配置，采用了默认值（25）
            log.warn("Cannot find the mailbox port configuration in the system settings, the default value (25) is used");
        }
        if(StrUtil.isEmpty(functionalEmailSmtpHost)) {
            // 在系统配置中找不到邮箱主机配置，无法发送邮件
            log.error("Cannot find the mailbox host configuration in the system settings, cannot send email");
        }
        if(StrUtil.isEmpty(functionalEmailSmtpEmail)) {
            // 在系统配置中找不到邮箱地址配置，无法发送邮件
            log.error("Cannot find the mailbox address configuration in the system settings, cannot send email");
        }
        if(StrUtil.isEmpty(functionalEmailSmtpUsername)) {
            // 在系统配置中找不到邮箱用户名配置，无法发送邮件
            log.error("Cannot find the mailbox username configuration in the system settings, cannot send email");
        }
        if(StrUtil.isEmpty(functionalEmailSmtpPassword)) {
            // 在系统配置中找不到邮箱密码配置，无法发送邮件
            log.error("Cannot find the mailbox password configuration in the system settings, cannot send email");
        }
        if(StrUtil.isEmpty(functionalEmailSmtpPort) || StrUtil.isEmpty(functionalEmailSmtpHost)
                || StrUtil.isEmpty(functionalEmailSmtpEmail) || StrUtil.isEmpty(functionalEmailSmtpUsername)
                || StrUtil.isEmpty(functionalEmailSmtpPassword)) {
            logCode();
        }


        // start: 设置邮箱端口
        // start: Set the mailbox port
        int port = 25;
        if (functionalEmailSmtpPort != null) {
            port = Integer.parseInt(functionalEmailSmtpPort);
        }
        // end: 设置邮箱端口
        // end: Set the mailbox port

        MailAccount account = new MailAccount();
        account.setHost(functionalEmailSmtpHost.strip());
        account.setPort(port);
        account.setAuth(true);
        account.setSslEnable(true);
        account.setFrom(functionalEmailSmtpEmail.strip());
        account.setUser(functionalEmailSmtpUsername.strip());
        account.setPass(functionalEmailSmtpPassword.strip());
        return account;
    }

}
