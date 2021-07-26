package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.AccountGoal;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountGoalDto {

    private Long id;
    private Long accountId;
    private Double value;
    private String name;
    private boolean completed;
    private Instant date;

    public static AccountGoalDto toDto(AccountGoal accountGoal) {
        return new AccountGoalDto(accountGoal.getId(), accountGoal.getAccountId(),
                accountGoal.getValue(), accountGoal.getName(), accountGoal.isCompleted(), accountGoal.getDate());

    }

    public static AccountGoal fromDto(AccountGoalDto accountGoalDto) {
        AccountGoal accountGoal = new AccountGoal();
        accountGoal.setAccountId(accountGoalDto.getAccountId());
        accountGoal.setId(accountGoalDto.getId());
        accountGoal.setValue(accountGoalDto.getValue());
        accountGoal.setName(accountGoalDto.getName());
        accountGoal.setDate(accountGoalDto.getDate());
        accountGoal.setCompleted(accountGoalDto.isCompleted());

        return accountGoal;
    }

}
