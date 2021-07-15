package ru.onlinewallet.util;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class PasswordUtil {
    public String decodeBtoaPassword(String pass){
        return new String(Base64.getDecoder().decode(pass));
    }
}