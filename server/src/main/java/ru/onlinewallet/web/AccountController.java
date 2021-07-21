package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import ru.onlinewallet.dto.account.AccountBillDto;
import ru.onlinewallet.dto.account.AccountDto;
import ru.onlinewallet.dto.account.AccountGoalDto;
import ru.onlinewallet.dto.account.ConvertedBalanceDto;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.entity.account.AccountGoal;
import ru.onlinewallet.service.AccountService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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

        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    private ResponseEntity<List<AccountDto>> getAll(@RequestParam("id") Long id) throws IOException,
            ParserConfigurationException, TransformerException, SAXException {
        return ResponseEntity.ok(
                accountService
                        .getAll(id)
                        .stream()
                        .map(acc -> {
                            AccountDto accountDto = AccountDto.toDto(acc);
                            try {
                                ConvertedBalanceDto balanceDto =
                                        ConvertedBalanceDto.toDto(accountService.getConvertedBalance(acc,
                                                acc.getAccountBills().get(0).getCurrency().getShortName()));
                                accountDto.setConvertedBalance(balanceDto);
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
