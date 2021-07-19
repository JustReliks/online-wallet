package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.account.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
