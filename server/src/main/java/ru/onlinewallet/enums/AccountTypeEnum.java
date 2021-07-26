package ru.onlinewallet.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountTypeEnum {
    CREDIT("CREDIT"),
    SALARY("SALARY"),
    SAVING("SAVING"),
    CUMULATIVE("CUMULATIVE");

    private final String name;

    public String getName() {
        return name;
    }
}
