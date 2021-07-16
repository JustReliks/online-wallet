package ru.onlinewallet.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onlinewallet.entity.user.UserSettings;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings,Long > {
    UserSettings findByUserId(Long userId);
}
