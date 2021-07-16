package ru.onlinewallet.service;

import com.nimbusds.jose.JOSEException;
import ru.onlinewallet.dto.security.ChangePassRequestDto;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.entity.user.UserSettings;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    Long save(User user) throws IOException;

    Optional<User> findByUsername(String username);

    User getUser(Long id);

    User getUser(String userName);

    JwtUserDetails authenticate(String username, String password) throws JOSEException;

    Optional<User> findByEmail(String email);

    User update(User user);

    User changeTwoFactorState(String user, String action);

    JwtUserDetails changePassword(ChangePassRequestDto changePassRequest) throws JOSEException;

    UserSettings getUserProfile(Long userId);

    UserSettings updateUserProfile(UserSettings userSettings);

    Long register(User user);
}

