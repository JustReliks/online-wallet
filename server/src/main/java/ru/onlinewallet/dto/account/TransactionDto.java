package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.Transaction;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private AccountDto account;
    private AccountBillDto accountBill;
    private Integer categoryId;
    private Instant dateTime;
    private Double quantity;
    private TransactionCategoryDto category;

    public static TransactionDto toDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setAccount(AccountDto.toDto(transaction.getAccount()));
        transactionDto.setAccountBill(AccountBillDto.toDto(transaction.getAccountBill()));
        transactionDto.setDateTime(transaction.getDateTime());
        transactionDto.setQuantity(transaction.getQuantity());
        transactionDto.setCategory(TransactionCategoryDto.toDto(transaction.getCategory()));

        return transactionDto;
    }

    public static Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setAccount(AccountDto.fromDto(transactionDto.getAccount()));
        transaction.setAccountBill(AccountBillDto.fromDto(transactionDto.getAccountBill()));
        transaction.setDateTime(transactionDto.getDateTime());
        transaction.setQuantity(transactionDto.getQuantity());
        transaction.setCategory(TransactionCategoryDto.fromDto(transactionDto.getCategory()));

        return transaction;
    }
}
