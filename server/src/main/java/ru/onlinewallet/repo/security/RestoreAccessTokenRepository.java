package ru.onlinewallet.repo.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.security.RestoreAccessToken;

public interface RestoreAccessTokenRepository extends JpaRepository<RestoreAccessToken, Long> {
    RestoreAccessToken findByToken(String token);
    void deleteByToken(String token);
}
