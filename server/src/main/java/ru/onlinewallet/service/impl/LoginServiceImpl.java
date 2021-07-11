package ru.onlinewallet.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.onlinewallet.dto.security.JwtRequestDto;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.service.LoginService;
import ru.onlinewallet.service.UserService;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Override
    public JwtUserDetails login(JwtRequestDto user) throws JOSEException, JsonProcessingException {
        String password = new String(Base64.getDecoder().decode(user.getPassword()));
        return userService.authenticate(user.getUsername(), password);
    }

    @Override
    public JwtUserDetails getCurrentUser() {
        return jwtUserDetailsService.getCurrentUser();
    }
}
