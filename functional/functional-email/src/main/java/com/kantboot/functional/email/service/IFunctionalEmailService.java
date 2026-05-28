package com.kantboot.functional.email.service;

import com.kantboot.functional.email.dto.EmailMessageDTO;

public interface IFunctionalEmailService {

    /**
     * 发送邮件
     * Send email
     * @param messageDTO 邮件信息
     *                   Email message
     */
    void send(EmailMessageDTO messageDTO);

}
