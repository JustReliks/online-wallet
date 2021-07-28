package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.account.TransactionCategory;

import java.util.List;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory,Long> {

    List<TransactionCategory> findAllByType(String code);

}
