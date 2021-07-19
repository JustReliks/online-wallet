package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.onlinewallet.dto.account.AccountDto;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.service.AccountService;

import java.util.Objects;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    private ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        Account account = accountService.createAccount(AccountDto.fromDto(accountDto));
        if (Objects.nonNull(account)) {
            return ResponseEntity.ok(AccountDto.toDto(account));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
