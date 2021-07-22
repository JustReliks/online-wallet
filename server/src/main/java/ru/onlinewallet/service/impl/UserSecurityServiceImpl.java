package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.security.RestoreAccessToken;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.repo.security.RestoreAccessTokenRepository;
import ru.onlinewallet.service.UserSecurityService;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {
    private final RestoreAccessTokenRepository restoreAccessTokenRepository;

    @Override
    public boolean validatePasswordResetToken(String token) {
        final RestoreAccessToken passToken = restoreAccessTokenRepository.findByToken(token);

        return isTokenFound(passToken) && !isTokenExpired(passToken);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        RestoreAccessToken myToken = new RestoreAccessToken(token, user);
        restoreAccessTokenRepository.save(myToken);
    }

    @Override
    public void delete(String token) {
        restoreAccessTokenRepository.deleteByToken(token);
    }

    private boolean isTokenFound(RestoreAccessToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(RestoreAccessToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
