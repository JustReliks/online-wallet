package ru.onlinewallet.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.Account;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private Long userId;
    private String name;
    private AccountGoalDto goal;
    private String description;
    private Instant createdAt;
    private Instant lastTransaction;
    private byte[] icon;
    private List<AccountBillDto> accountBills;

    public static AccountDto toDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setUserId(account.getUserId());
        accountDto.setName(account.getName());
        accountDto.setDescription(account.getDescription());
        accountDto.setCreatedAt(account.getCreatedAt());
        accountDto.setLastTransaction(account.getLastTransaction());
        accountDto.setIcon(account.getIcon());
        accountDto.setAccountBills(
                account
                        .getAccountBills()
                        .stream()
                        .map(AccountBillDto::toDto)
                        .collect(Collectors.toList())
        );

        return accountDto;
    }

    public static Account fromDto(AccountDto dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setUserId(dto.getUserId());
        account.setName(dto.getName());
        account.setDescription(dto.getDescription());
        account.setCreatedAt(Objects.isNull(dto.getCreatedAt()) ? Instant.now() : dto.getCreatedAt());
        account.setLastTransaction(Objects.isNull(dto.getLastTransaction()) ? Instant.now() : dto.getLastTransaction());
        account.setIcon(dto.getIcon());
//        account.setAccountBills(
//                dto
//                        .getAccountBills()
//                        .stream()
//                        .map(AccountBillDto::fromDto)
//                        .collect(Collectors.toList())
//        );

        return account;
    }
}
