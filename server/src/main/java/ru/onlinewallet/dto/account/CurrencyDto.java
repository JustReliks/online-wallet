package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private Long id;
    private String shortName;
    private String longName;

    public static CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(currency.getId(), currency.getShortName(), currency.getLongName());
    }

    public static Currency fromDto(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        currency.setId(currencyDto.getId());
        currency.setShortName(currencyDto.getShortName());
        currency.setLongName(currencyDto.getLongName());
        return currency;
    }
}
