package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.account.Transaction;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);

    List<Transaction> findAllByAccountId(Long accountId);
}
