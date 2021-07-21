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

    public static TransactionDto toDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setAccount(AccountDto.toDto(transaction.getAccount()));
        transactionDto.setAccountBill(AccountBillDto.toDto(transaction.getAccountBill()));
        transactionDto.setCategoryId(transaction.getCategoryId());
        transactionDto.setDateTime(transaction.getDateTime());
        transactionDto.setQuantity(transaction.getQuantity());

        return transactionDto;
    }

    public static Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setAccount(AccountDto.fromDto(transactionDto.getAccount()));
        transaction.setAccountBill(AccountBillDto.fromDto(transactionDto.getAccountBill()));
        transaction.setCategoryId(transactionDto.getCategoryId());
        transaction.setDateTime(transactionDto.getDateTime());
        transaction.setQuantity(transactionDto.getQuantity());

        return transaction;
    }
}
