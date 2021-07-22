package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.entity.account.Transaction;
import ru.onlinewallet.repo.account.TransactionHistoryRepository;
import ru.onlinewallet.service.TransactionHistoryService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public Transaction addTransaction(AccountBill accountBillId, Long userId, Long categoryId, double quantity,
                                      Instant now) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountBillId.getAccountId());
        transaction.setUserId(userId);
        transaction.setBillId(accountBillId.getId());
        transaction.setCategoryId(categoryId);
        transaction.setQuantity(quantity);
        transaction.setDateTime(now);

        return transactionHistoryRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions(Long userId) {
        return transactionHistoryRepository.findAllByUserId(userId);
    }
}
