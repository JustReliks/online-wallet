package ru.onlinewallet.dto.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.onlinewallet.dto.UserDtoBase;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.security.JwtUserDetails;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JwtUserDto extends UserDtoBase {
    private String token;

    public JwtUserDto(User user, String token) {
        super(user);
        this.token = token;
    }

    public JwtUserDto(JwtUserDetails userDetails, String token) {
        super(userDetails);
        this.token = token;
    }

    public JwtUserDto(JwtUserDetails userDetails) {
        super(userDetails);
        this.token = userDetails.getToken();
    }
}
