package ru.onlinewallet.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.security.JwtRequestDto;
import ru.onlinewallet.dto.security.JwtUserDto;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.service.LoginService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final GoogleAuthenticator gAuth;

    @PostMapping
    public ResponseEntity<JwtUserDto> login(@RequestBody JwtRequestDto user) throws JOSEException,
            JsonProcessingException {
        JwtUserDetails login = loginService.login(user);
        if (login.isTwoFactorEnabled()) {
            JwtUserDto jwtUserDto = new JwtUserDto();
            jwtUserDto.setUsername(login.getUsername());
            jwtUserDto.setTwoFactorEnabled(login.isTwoFactorEnabled());
            return ResponseEntity.ok(jwtUserDto);
        }
        return ResponseEntity.ok(new JwtUserDto(loginService.login(user)));
    }

    @PostMapping("/two-factor")
    public ResponseEntity<JwtUserDto> loginTwoFactor(@RequestBody JwtRequestDto user) throws JsonProcessingException,
            JOSEException {
        boolean twoFactorAuthorized = gAuth.authorizeUser(user.getUsername(), user.getTwoFactorKey());
        if (!twoFactorAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(new JwtUserDto(loginService.login(user)));
    }

    @GetMapping("/current")
    public ResponseEntity<JwtUserDto> getCurrentUser() {
        try {
            return ResponseEntity.ok(new JwtUserDto(loginService.getCurrentUser()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
