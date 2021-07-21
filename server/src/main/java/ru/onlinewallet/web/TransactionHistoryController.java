package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.onlinewallet.dto.account.TransactionDto;
import ru.onlinewallet.entity.account.Transaction;
import ru.onlinewallet.service.TransactionHistoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping
    private ResponseEntity<List<TransactionDto>> getAllTransactions(@RequestParam("userId") Long userId) {

        List<Transaction> transactions = transactionHistoryService.getAllTransactions(userId);
        return ResponseEntity.ok(transactions
                .stream()
                .map(TransactionDto::toDto)
                .collect(Collectors.toList()));
    }

}
