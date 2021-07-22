package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.entity.account.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionHistoryService {
    Transaction addTransaction(AccountBill accountBillId, Long userId, Long categoryId, double quantity, Instant now);

    List<Transaction> getAllTransactions(Long userId);

}
