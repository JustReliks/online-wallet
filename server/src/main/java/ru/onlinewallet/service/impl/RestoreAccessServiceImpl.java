package ru.onlinewallet.service.impl;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.dto.ResponseMessage;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.service.MailService;
import ru.onlinewallet.service.RestoreAccessService;
import ru.onlinewallet.service.UserSecurityService;
import ru.onlinewallet.service.UserService;
import ru.onlinewallet.template.EmailTemplates;
import ru.onlinewallet.util.PasswordUtil;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestoreAccessServiceImpl implements RestoreAccessService {

    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final MailService mailService;
    private final String RESTORE_URL = "http://online-wallet.ru/restore";
    private final String RESTORE_ACCESS_SUBJECT = "Восстановление доступа к аккаунту.";
    private final String RESTORE_ACCESS_FROM = "Online-wallet.ru - бесплатный, понятный и удобный финансовый онлайн менеджер";


    @Override
    public ResponseMessage restoreAccessRequest(String userAuthParam, String ip) throws MessagingException,
            UnsupportedEncodingException {
        User user = userAuthParam.contains("@")
                ? userService.findByEmail(userAuthParam).orElseThrow(EntityNotFoundException::new)
                : userService.getUser(userAuthParam);

        String token = UUID.randomUUID().toString();
        userSecurityService.createPasswordResetTokenForUser(user, token);
        String template = buildRestoreAccessMailTemplate(user, token, ip);
        mailService.sendHtmlMessage(RESTORE_ACCESS_FROM, user.getEmail(), RESTORE_ACCESS_SUBJECT, template);
        return new ResponseMessage("Дальнейшие инструкции по восстановлению доступа над аккаунтом высланы на почту " + codeEmail(user.getEmail()));
    }

    @Override
    @Transactional
    public ResponseMessage resetPassword(Long userId, String token) throws MessagingException, JOSEException,
            UnsupportedEncodingException {
        validateToken(token);
        userSecurityService.delete(token);
        String password = PasswordUtil.generateRandomPassword(9, 48, 122);
        User user = userService.getUser(userId);
        user = userService.changePassword(user, password);
        userService.update(user);
        String template = buildRestorePasswordMailTemplate(user.getUsername(), password);
        mailService.sendHtmlMessage(RESTORE_ACCESS_FROM, user.getEmail(), RESTORE_ACCESS_SUBJECT, template);
        return new ResponseMessage("Письмо с новым паролем выслано на почту " + codeEmail(user.getEmail()));
    }

    private void validateToken(String token) {
        if (!userSecurityService.validatePasswordResetToken(token)) {
            throw new AccountExpiredException("Время жизни ссылки истекло (2 часа с момента создания), либо ссылка " +
                    "неверна.");
        }
    }

    @Override
    public ResponseMessage resetTwoFactor(Long userId, String token) {
        validateToken(token);
        User user = userService.getUser(userId);
        user.setIsTwoFactorEnabled(false);
        userService.update(user);
        return new ResponseMessage("Двухфакторная аутентификация успешно отключена.");
    }

    private String codeEmail(String email) {
        int start = email.indexOf("@");

        if (start < 0) {
            return "";
        }

        StringBuilder sbEmail = new StringBuilder(email);
        sbEmail.replace(1, start - 1, "******");
        return sbEmail.toString();
    }

    private String buildRestoreAccessMailTemplate(User user, String token, String ip) {
        //http://localhost:4200/restore?userId=Mrcreative&action=reset-password&token=123124124124124
        String template = EmailTemplates.restoreAccessMailTemplate;
        return template
                .replace("USERNAME", user.getUsername())
                .replace("SENDER_IP_ADDRESS", ip)
                .replace("RESET_PASSWORD_URL", RESTORE_URL + "?userId=" + user.getId() + "&action=reset-password" +
                        "&token=" + token)
                .replace("RESET_TWO_FACTOR_AUTH", RESTORE_URL + "?userId=" + user.getId() + "&action=reset-two-factor" +
                        "&token=" + token);
    }

    private String buildRestorePasswordMailTemplate(String username, String password) {
        String template = EmailTemplates.restorePasswordTemplate;
        return template
                .replace("USERNAME", username)
                .replace("PASSWORD_REPLACE", password);
    }
}
