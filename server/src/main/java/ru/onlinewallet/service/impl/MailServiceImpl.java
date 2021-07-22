package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.onlinewallet.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendHtmlMessage(String from, String to, String subject, String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("no-reply@online-wallet.ru","Online-wallet.ru");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setSubject(subject);
        emailSender.send(mimeMessage);
    }

    public String buildRestoreAccessEmailText() {
        return null;
    }
}
