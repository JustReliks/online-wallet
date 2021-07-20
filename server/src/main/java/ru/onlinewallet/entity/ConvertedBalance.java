package ru.onlinewallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConvertedBalance {
    private final String currency;
    private final double value;
}
