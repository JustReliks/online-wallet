package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.account.AccountBillDto;
import ru.onlinewallet.dto.account.AccountDto;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.service.AccountService;

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
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    private ResponseEntity<List<AccountDto>> getAll(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                accountService
                        .getAll(id)
                        .stream()
                        .map(AccountDto::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/bill")
    private ResponseEntity<AccountBillDto> addTransaction(@RequestBody AccountBillDto dto,
                                                          @RequestParam("plus") boolean isPlus,
                                                          @RequestParam("value") double value) {
        AccountBill accountBill = AccountBillDto.fromDto(dto);
        AccountBill bill = accountService.addTransaction(accountBill, isPlus, value);
        return ResponseEntity.ok(AccountBillDto.toDto(bill));
    }
}
