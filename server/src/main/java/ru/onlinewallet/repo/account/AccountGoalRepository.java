package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.account.AccountGoal;

@Repository
public interface AccountGoalRepository extends JpaRepository<AccountGoal, Long> {
}
