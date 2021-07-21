package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.account.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
}
