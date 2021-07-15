package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.security.UserTOTP;
import ru.onlinewallet.repo.security.UserTotpRepository;
import ru.onlinewallet.service.TwoFactorService;

@Service
@RequiredArgsConstructor
public class TwoFactorServiceImpl implements TwoFactorService {

    private final UserTotpRepository userTotpRepository;

    @Override
    @Transactional
    public void deleteUserTotp(String username) {
        userTotpRepository.deleteByUsername(username);
    }

    @Override
    public void setNeedRegenerateState(String username, boolean needRegenerateState) {
        UserTOTP byUsername = userTotpRepository.findByUsername(username);
        byUsername.setNeedRegenerate(needRegenerateState);
        userTotpRepository.save(byUsername);
    }
}
