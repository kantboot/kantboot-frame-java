package com.kantboot.functional.email.service.service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.kantboot.functional.email.constants.MessageStatusCodeConstants;
import com.kantboot.functional.email.domain.entity.FunctionalEmail;
import com.kantboot.functional.email.dto.EmailMessageDTO;
import com.kantboot.functional.email.exception.FunctionalEmailException;
import com.kantboot.functional.email.repository.FunctionalEmailRepository;
import com.kantboot.functional.email.service.IFunctionalEmailService;
import com.kantboot.functional.email.setting.FunctionalEmailSetting;
import com.kantboot.util.event.annotation.EventOn;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FunctionalEmailServiceImpl implements IFunctionalEmailService {

    @Resource
    private FunctionalEmailRepository repository;

    @Resource
    private FunctionalEmailSetting setting;

    @Resource
    private EventEmit eventEmit;

    @Override
    public void send(EmailMessageDTO messageDTO) {
        eventEmit.to("sendEmail", messageDTO);
    }

    @EventOn(code="sendEmail")
    private void sendEmail(EmailMessageDTO messageDTO) {
        FunctionalEmail functionalEmail = getFunctionalEmail(messageDTO);
        // 保存消息
        // Save message
        FunctionalEmail save = repository.save(functionalEmail);

        MailAccount mailAccount = setting.getMailAccount();
        try {
            MailUtil.send(mailAccount, save.getTo(), save.getSubject(), save.getContent(), save.getIsHtml());
        } catch (Exception e) {
            // 输出异常
            e.printStackTrace();
            // 异常信息
            // Exception information
            BaseException emailServerError = FunctionalEmailException.EMAIL_SERVER_ERROR;
            // 发送失败，更新消息状态
            // Send failed, update message status
            save.setMessageStatusCode(MessageStatusCodeConstants.STATUS_CODE_FAIL);
            // 设置失败原因编码
            // Set the failure reason code
            save.setMessageStatusFailReasonCode(emailServerError.getStateCode());
            // 设置失败原因
            // Set the reason for failure
            save.setMessageStatusFailReason(emailServerError.getMessage());
            // 保存失败消息
            // Save failed message
            repository.save(save);
            // 抛出异常，提示发送失败
            // Throw an exception to indicate that the sending failed
            throw emailServerError;
        }
        save.setMessageStatusCode(MessageStatusCodeConstants.STATUS_CODE_SUCCESS);
        repository.save(save);
    }


    private FunctionalEmail getFunctionalEmail(EmailMessageDTO messageDTO) {
        FunctionalEmail functionalEmail = new FunctionalEmail();
        // 设置接收者
        functionalEmail.setTo(messageDTO.getEmail().trim());
        // 设置主题
        functionalEmail.setSubject(messageDTO.getSubject());
        // 设置内容
        functionalEmail.setContent(messageDTO.getContent());
        Boolean isHtml = messageDTO.getIsHtml();
        if (isHtml == null) {
            isHtml = false;
        }
        // 设置是否是html
        // Set whether it is html
        functionalEmail.setIsHtml(isHtml);
        // 设置消息状态
        // Set message status
        functionalEmail.setMessageStatusCode(MessageStatusCodeConstants.STATUS_CODE_SENDING);
        return functionalEmail;
    }

}