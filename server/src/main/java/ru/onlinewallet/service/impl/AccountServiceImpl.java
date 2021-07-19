package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.repo.account.AccountRepository;
import ru.onlinewallet.service.AccountService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
