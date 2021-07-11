package ru.onlinewallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinewallet.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
