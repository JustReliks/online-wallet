package ru.onlinewallet.dto.security;

import lombok.Data;

@Data
public class ChangePassRequestDto {
    private Long userId;
    private String oldPass;
    private String newPass;
    private String confirmNewPass;
}
