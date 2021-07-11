package ru.onlinewallet.dto.security;

import lombok.Data;

@Data
public class ValidateCodeDto {
    private String username;
    private Integer code;
}
