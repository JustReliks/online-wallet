package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    List<AccountBill> createAccountBills(Long id, List<AccountBill> collect);
}
