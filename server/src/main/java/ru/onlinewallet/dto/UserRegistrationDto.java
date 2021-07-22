package ru.onlinewallet.dto;

import lombok.Data;
import ru.onlinewallet.dto.account.CurrencyDto;
import ru.onlinewallet.entity.user.User;


@Data
public class UserRegistrationDto {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String currency;

    public static User fromDto(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
