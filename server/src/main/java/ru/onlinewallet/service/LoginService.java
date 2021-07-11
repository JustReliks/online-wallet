package ru.onlinewallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import ru.onlinewallet.dto.security.JwtRequestDto;
import ru.onlinewallet.entity.security.JwtUserDetails;

public interface LoginService {

    JwtUserDetails login(JwtRequestDto user) throws JOSEException, JsonProcessingException;

    JwtUserDetails getCurrentUser();

}
