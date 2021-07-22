package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.ResponseMessage;
import ru.onlinewallet.service.RestoreAccessService;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/restore-access")
@RequiredArgsConstructor
public class RestoreAccessController {

    private final RestoreAccessService restoreAccessService;

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    @PostMapping
    public ResponseEntity<ResponseMessage> restoreAccess(@RequestParam("param") String userAuthParam,
                                                         HttpServletRequest request) throws MessagingException {
        try {
            String ip = request.getRemoteAddr();
            return ResponseEntity.ok().body(restoreAccessService.restoreAccessRequest(userAuthParam,
                    fetchClientIpAddr(request)));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("Пользователь с таким логином" +
                    " или почтой не найден."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Возникла ошибка при " +
                    "восстановление доступа. Попробуйте позже."));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> restoreAccessGet(@RequestParam("userId") Long userId,
                                                            @RequestParam("action") String action,
                                                            @RequestParam("token") String token) {
        try {
            if (action.equals("reset-password")) {
                return ResponseEntity.ok(restoreAccessService.resetPassword(userId, token));
            } else if (action.equals("reset-two-factor")) {
                return ResponseEntity.ok(restoreAccessService.resetTwoFactor(userId, token));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Неизвестный запрос. Попробуйте еще раз."));
            }
        } catch (AccountExpiredException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("Время жизни ссылки истекло " +
                    "(2 часа с момента создания), либо она неверна."));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Возникла ошибка. Попробуйте позже."));
        }
    }

    private String fetchClientIpAddr(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
