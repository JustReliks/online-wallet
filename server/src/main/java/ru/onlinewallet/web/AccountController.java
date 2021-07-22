package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.account.*;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.entity.account.AccountGoal;
import ru.onlinewallet.entity.account.AccountType;
import ru.onlinewallet.service.AccountService;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    private ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto) {
        Account account = accountService.createAccount(AccountDto.fromDto(dto));
        if (Objects.isNull(account)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<AccountBill> collect =
                dto.getAccountBills().stream().map(AccountBillDto::fromDto).collect(Collectors.toList());
        List<AccountBill> list = accountService.createAccountBills(account.getId(), collect);
        List<AccountBillDto> billDto = list.stream().map(AccountBillDto::toDto).collect(Collectors.toList());

        AccountDto accountDto = AccountDto.toDto(account);
        accountDto.setAccountBills(billDto);
        AccountGoalDto goal1 = dto.getGoal();
        if (Objects.nonNull(goal1)) {
            AccountGoal goal = AccountGoalDto.fromDto(dto.getGoal());
            goal.setAccountId(account.getId());
            AccountGoalDto accountGoalDto = AccountGoalDto.toDto(accountService.saveGoal(goal));
            accountDto.setGoal(accountGoalDto);
        }
        AccountTypeDto accountTypeDto = dto.getAccountType();
        AccountType accountType = AccountTypeDto.fromDto(accountTypeDto);
        accountType.setAccountId(account.getId());
        AccountType aType = accountService.createAccountType(accountType);
        accountDto.setAccountType(AccountTypeDto.toDto(aType));

        return ResponseEntity.ok(accountDto);
    }

    @PutMapping
    private ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto dto) {

        Account account = AccountDto.fromDto(dto);
        if (Objects.nonNull(dto.getGoal()))
            account.setGoal(AccountGoalDto.fromDto(dto.getGoal()));
        Account updateAccount = accountService.updateAccount(account);
        AccountDto accountDto = AccountDto.toDto(updateAccount);
        return ResponseEntity.ok(accountDto);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    private ResponseEntity<Boolean> deleteAccount(@PathParam("id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(true);
    }


    @GetMapping
    private ResponseEntity<List<AccountDto>> getAll(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                accountService
                        .getAll(id)
                        .stream()
                        .map(acc -> {
                            AccountDto accountDto = AccountDto.toDto(acc);
                            try {
                                if (acc.getAccountBills().size() > 0) {
                                    ConvertedBalanceDto balanceDto =
                                            ConvertedBalanceDto.toDto(accountService.getConvertedBalance(acc,
                                                    acc.getAccountBills().get(0).getCurrency().getShortName()));
                                    accountDto.setConvertedBalance(balanceDto);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return accountDto;
                        })
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/balance")
    private ResponseEntity<ConvertedBalanceDto> getBalance(@RequestParam("id") Long id,
                                                           @RequestParam("currency") String currency) throws IOException {
        ConvertedBalance convertedBalance = accountService.getConvertedBalance(id, currency);
        return ResponseEntity.ok(ConvertedBalanceDto.toDto(convertedBalance));
    }

    @PostMapping("/bill")
    private ResponseEntity<AccountBillDto> addTransaction(@RequestBody AccountBillDto dto,
                                                          @RequestParam("userId") Long userId,
                                                          @RequestParam("plus") boolean isPlus,
                                                          @RequestParam("value") double value) {
        AccountBill accountBill = AccountBillDto.fromDto(dto);
        AccountBill bill = accountService.addTransaction(accountBill, userId, isPlus, value);
        return ResponseEntity.ok(AccountBillDto.toDto(bill));
    }


}
