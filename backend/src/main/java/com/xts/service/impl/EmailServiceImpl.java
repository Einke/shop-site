package com.xts.service.impl;

import com.xts.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendSimpleEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);  // true 表示HTML邮件
            messageHelper.setFrom("2426446807@qq.com");  // 发件人

            mailSender.send(mimeMessage);
            log.info("发送邮件改变订单状态");
            return true;
        } catch (MailException | jakarta.mail.MessagingException e) {
            log.error("发送邮件失败",e);
            return false;
        }
    }
}
