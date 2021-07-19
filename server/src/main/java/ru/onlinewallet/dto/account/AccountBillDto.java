package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.AccountBill;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBillDto {
    private Long id;
    private Long accountId;
    private CurrencyDto currency;
    private Double balance;

    public static AccountBillDto toDto(AccountBill accountBill) {
        return new AccountBillDto(accountBill.getId(), accountBill.getAccountId(),
                CurrencyDto.toDto(accountBill.getCurrency()), accountBill.getBalance());
    }

    public static AccountBill fromDto(AccountBillDto accountBillDto) {
        AccountBill accountBill = new AccountBill();
        accountBill.setId(accountBillDto.getId());
        accountBill.setAccountId(accountBillDto.getAccountId());
        accountBill.setCurrency(CurrencyDto.fromDto(accountBillDto.getCurrency()));
        accountBill.setBalance(Objects.isNull(accountBillDto.getBalance()) ? 0 : accountBillDto.getBalance());

        return accountBill;
    }
}
