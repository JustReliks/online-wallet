package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.account.TransactionCategory;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory,Long> {
}
