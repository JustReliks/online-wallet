package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.account.AccountBill;

public interface AccountBillRepository extends JpaRepository<AccountBill, Long> {
}
