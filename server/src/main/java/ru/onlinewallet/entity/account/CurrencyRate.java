package ru.onlinewallet.entity.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRate {
    private final String currency;
    private double rate;
}
