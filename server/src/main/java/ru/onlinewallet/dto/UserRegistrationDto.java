package ru.onlinewallet.dto;

import lombok.Data;
import ru.onlinewallet.entity.User;


@Data
public class UserRegistrationDto {

    private String username;
    private String password;
    private String email;

    public static User fromDto(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
