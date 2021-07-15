package ru.onlinewallet.service;

public interface TwoFactorService {

    void deleteUserTotp(String username);

    void setNeedRegenerateState(String username, boolean needRegenerateState);
}
