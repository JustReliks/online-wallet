package ru.onlinewallet.service;

import ru.onlinewallet.entity.user.User;

import java.io.IOException;

public interface RegistrationService {

    Long register(User user) throws IOException;

    boolean checkUserExistByUserName(String username);

    boolean checkUserExistByUserEmail(String email);
}
