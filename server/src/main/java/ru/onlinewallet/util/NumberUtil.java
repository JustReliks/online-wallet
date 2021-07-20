package ru.onlinewallet.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class NumberUtil {
    public double round(Double number) {
        return new BigDecimal(number.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
