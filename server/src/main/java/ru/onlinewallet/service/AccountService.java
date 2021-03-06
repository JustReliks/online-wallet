package ru.onlinewallet.service;

import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    List<AccountBill> createAccountBills(Long id, List<AccountBill> collect);

    List<Account> getAll(Long userId);

    AccountBill addTransaction(AccountBill accountBill, Long userId, boolean isPlus, double value, Long categoryId, Instant transactionDate) throws IOException;

    Double convertCurrencies(double value, String from, String to) throws IOException;

    ConvertedBalance getConvertedBalance(Account account, String currency) throws IOException;

    ConvertedBalance getConvertedBalance(Long id, String currency) throws IOException;

    AccountGoal saveGoal(AccountGoal goal);

    Account updateAccount(Account account) throws IOException;

    List<AccountType> getAllAccountTypes();

    AccountType createAccountType(AccountType accountType);

    void deleteAccount(Long id);

    CreditInfo calculateCreditInfo(Account acc);

    Double calculateGoalDailyPayment(Account acc) throws IOException;

    int getCountAllAccounts(Long userId);

}
