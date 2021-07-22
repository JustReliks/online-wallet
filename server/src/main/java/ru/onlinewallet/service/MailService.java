package ru.onlinewallet.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendHtmlMessage(String from,
                         String to, String subject, String text) throws MessagingException, UnsupportedEncodingException;

}
