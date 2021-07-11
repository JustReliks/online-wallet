package ru.onlinewallet.repo.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.security.UserTOTP;

@Repository
public interface UserTotpRepository extends JpaRepository<UserTOTP, Long> {
    UserTOTP findByUsername(String username);

    void deleteByUsername(String username);
}
