package ru.onlinewallet.repo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.account.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByCode(String code);

}
