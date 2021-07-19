package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.repo.account.AccountBillRepository;
import ru.onlinewallet.repo.account.AccountRepository;
import ru.onlinewallet.service.AccountService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountBillRepository accountBillRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<AccountBill> createAccountBills(Long id, List<AccountBill> collect) {
        if (Objects.isNull(id)) {
            throw new RuntimeException("Id счета не может быть null");
        }

        collect.forEach(bill -> bill.setAccountId(id));

        return accountBillRepository.saveAll(collect);
    }

    @Override
    public List<Account> getAll(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }
}
