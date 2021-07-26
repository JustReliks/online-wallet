package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.AccountBill;

import java.time.Instant;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBillDto {
    private Long id;
    private Long accountId;
    private CurrencyDto currency;
    private Double balance;
    private Double startBalance;
    private Double rate;
    private Instant maturityDate;

    public static AccountBillDto toDto(AccountBill accountBill) {
        return new AccountBillDto(accountBill.getId(), accountBill.getAccountId(),
                CurrencyDto.toDto(accountBill.getCurrency()), accountBill.getBalance(), accountBill.getStartBalance()
                , accountBill.getRate(), accountBill.getMaturityDate());
    }

    public static AccountBill fromDto(AccountBillDto accountBillDto) {
        AccountBill accountBill = new AccountBill();
        accountBill.setId(accountBillDto.getId());
        accountBill.setAccountId(accountBillDto.getAccountId());
        accountBill.setCurrency(CurrencyDto.fromDto(accountBillDto.getCurrency()));
        accountBill.setStartBalance(accountBillDto.getStartBalance());
        accountBill.setBalance(Objects.isNull(accountBillDto.getBalance()) ? 0 : accountBillDto.getBalance());
        accountBill.setRate((Objects.isNull(accountBillDto.getRate()) ? 1 : accountBillDto.getRate()));
        accountBill.setMaturityDate(accountBillDto.getMaturityDate());

        return accountBill;
    }
}
