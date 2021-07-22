package ru.onlinewallet.service;

import com.nimbusds.jose.JOSEException;
import ru.onlinewallet.dto.ResponseMessage;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface RestoreAccessService {
    ResponseMessage restoreAccessRequest(String userAuthParam, String ip) throws MessagingException, UnsupportedEncodingException;

    ResponseMessage resetPassword(Long userId, String token) throws MessagingException, JOSEException,
            UnsupportedEncodingException;

    ResponseMessage resetTwoFactor(Long userId, String token);
}
