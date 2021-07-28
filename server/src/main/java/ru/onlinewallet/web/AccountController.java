package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.account.*;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.*;
import ru.onlinewallet.service.AccountService;
import ru.onlinewallet.service.DemoService;
import ru.onlinewallet.service.StatisticsService;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.onlinewallet.enums.AccountTypeEnum.CREDIT;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final DemoService demoService;

    @PostMapping
    private ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto) {
        Account account = accountService.createAccount(AccountDto.fromDto(dto));
        if (Objects.isNull(account)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Instant now = Instant.now();
        if (dto.getAccountType().getType().getId() == 1) {
            if (Objects.isNull(dto.getFreezeDate()) || dto.getFreezeDate().isBefore(now)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        AccountDto accountDto = AccountDto.toDto(account);
        AccountTypeDto accountTypeDto = dto.getAccountType();
        AccountType accountType = AccountTypeDto.fromDto(accountTypeDto);
        accountType.setAccountId(account.getId());
        AccountType aType = accountService.createAccountType(accountType);
        accountDto.setAccountType(AccountTypeDto.toDto(aType));

        List<AccountBill> collect =
                dto.getAccountBills().stream().map(bill -> {
                    AccountBill accountBill = AccountBillDto.fromDto(bill);
                    if (accountDto.getAccountType().getType().getCode().equals(CREDIT.getName())) {
                        double balance = accountBill.getBalance() * -1;
                        accountBill.setStartBalance(balance);
                        accountBill.setBalance(balance);
                    }
                    return accountBill;
                }).collect(Collectors.toList());
        List<AccountBill> list = accountService.createAccountBills(account.getId(), collect);
        List<AccountBillDto> billDto = list.stream().map(AccountBillDto::toDto).collect(Collectors.toList());

        accountDto.setAccountBills(billDto);
        AccountGoalDto goal1 = dto.getGoal();
        if (Objects.nonNull(goal1)) {
            if (goal1.getDate().isBefore(now)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            AccountGoal goal = AccountGoalDto.fromDto(dto.getGoal());
            goal.setAccountId(account.getId());
            AccountGoalDto accountGoalDto = AccountGoalDto.toDto(accountService.saveGoal(goal));
            accountDto.setGoal(accountGoalDto);
        }

        return ResponseEntity.ok(accountDto);
    }

    @PutMapping
    private ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto dto) throws IOException {
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
    private ResponseEntity<List<AccountDto>> getAll(@RequestParam("id") Long id) throws IOException {
//        demoService.generateAccounts(id);
        return ResponseEntity.ok(
                accountService
                        .getAll(id)
                        .stream()
                        .map(acc -> {
                            try {
                                AccountDto accountDto = AccountDto.toDto(acc);
                                if (acc.getAccountType().getType().getCode().equals(CREDIT.getName())) {
                                    CreditInfo creditInfo = accountService.calculateCreditInfo(acc);
                                    accountDto.setCreditInfo(CreditInfoDto.toDto(creditInfo));
                                }

                                if (Objects.nonNull(accountDto.getGoal())) {
                                    accountDto.getGoal().setDailyPayment(accountService.calculateGoalDailyPayment(acc));
                                }

                                if (acc.getAccountBills().size() > 0) {
                                    ConvertedBalanceDto balanceDto =
                                            ConvertedBalanceDto.toDto(accountService.getConvertedBalance(acc,
                                                    acc.getAccountBills().get(0).getCurrency().getShortName()));
                                    accountDto.setConvertedBalance(balanceDto);
                                }
                                return accountDto;
                            } catch (Exception e) {
                                return null;
                            }
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
                                                          @RequestParam("value") double value,
                                                          @RequestParam("category") Long categoryId) throws IOException {
        AccountBill bill;
        try {
            AccountBill accountBill = AccountBillDto.fromDto(dto);
            bill = accountService.addTransaction(accountBill, userId, isPlus, value, categoryId, Instant.now());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(AccountBillDto.toDto(bill));
    }

    @GetMapping("/convert")
    private double convertCurrencies(@RequestParam String from,
                                     @RequestParam String to,
                                     @RequestParam Double value) throws IOException {
        return this.accountService.convertCurrencies(value, from, to);
    }

    @GetMapping("/count")
    private int getCountAllAccounts(@RequestParam("userId") Long userId) {
        return this.accountService.getCountAllAccounts(userId);
    }
}
