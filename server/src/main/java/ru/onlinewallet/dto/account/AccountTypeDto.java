package ru.onlinewallet.dto.account;

import lombok.Data;
import ru.onlinewallet.entity.account.AccountType;

@Data
public class AccountTypeDto {

    private Long id;
    private Long accountId;
    private TypeDto type;

    public static AccountTypeDto toDto(AccountType accountType) {
        AccountTypeDto accountTypeDto = new AccountTypeDto();
        accountTypeDto.setId(accountType.getId());
        accountTypeDto.setAccountId(accountType.getAccountId());
        accountTypeDto.setType(TypeDto.toDto(accountType.getType()));

        return accountTypeDto;
    }

    public static AccountType fromDto(AccountTypeDto accountTypeDto) {
        AccountType accountType = new AccountType();
        accountType.setId(accountTypeDto.getId());
        accountType.setAccountId(accountTypeDto.getAccountId());
        accountType.setTypeId(accountTypeDto.getType().getId());
        accountType.setType(TypeDto.fromDto(accountTypeDto.getType()));

        return accountType;
    }
}

