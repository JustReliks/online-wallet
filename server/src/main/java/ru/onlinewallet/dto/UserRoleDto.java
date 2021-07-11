package ru.onlinewallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.onlinewallet.entity.Role;

@Data
@NoArgsConstructor
public class UserRoleDto {
    private long id;
    private String name;

    public UserRoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();

    }

    public UserRoleDto(GrantedAuthority authority) {
        this.name = authority.getAuthority();
    }
}
