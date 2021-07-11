package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.User;
import ru.onlinewallet.service.RegistrationService;
import ru.onlinewallet.service.UserService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Transactional
    public Long register(User user) throws IOException{
        return userService.save(user);
    }

    @Override
    public boolean checkUserExistByUserName(String username) {
        return userService.findByUsername(username).isPresent();
    }

    @Override
    public boolean checkUserExistByUserEmail(String email) {
        return userService.findByEmail(email).isPresent();
    }

}
