package ru.onlinewallet.dto.account;

import lombok.Data;
import ru.onlinewallet.entity.ConvertedBalance;

@Data
public class ConvertedBalanceDto {
    private double balance;
    private String currency;

    public static ConvertedBalanceDto toDto(ConvertedBalance convertedBalance) {
        ConvertedBalanceDto convertedBalanceDto = new ConvertedBalanceDto();
        convertedBalanceDto.setBalance(convertedBalance.getValue());
        convertedBalanceDto.setCurrency(convertedBalance.getCurrency());
        return convertedBalanceDto;
    }
}
