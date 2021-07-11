package ru.onlinewallet.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestDto {
    private String username;
    private String password;
    private Integer twoFactorKey;
}
