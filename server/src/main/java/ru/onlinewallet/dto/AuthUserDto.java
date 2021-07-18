package ru.onlinewallet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.onlinewallet.dto.security.JwtUserDto;
import ru.onlinewallet.entity.security.JwtUserDetails;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthUserDto extends JwtUserDto {
    private byte[] profileImage;

    public AuthUserDto(JwtUserDetails userDetails, byte[] profileImage) {
        super(userDetails);
        this.profileImage = profileImage;
    }
}
