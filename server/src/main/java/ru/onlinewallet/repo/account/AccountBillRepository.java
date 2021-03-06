package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.account.AccountBill;

@Repository
public interface AccountBillRepository extends JpaRepository<AccountBill, Long> {
}
