package ru.onlinewallet.service;


import ru.onlinewallet.entity.user.User;

public interface UserSecurityService {
    boolean validatePasswordResetToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    void delete(String token);
}
