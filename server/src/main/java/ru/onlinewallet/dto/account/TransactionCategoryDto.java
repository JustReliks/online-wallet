package ru.onlinewallet.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.account.TransactionCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryDto {

    private Long id;
    private String code;
    private String type;
    private String title;
    private byte[] icon;

    public static TransactionCategoryDto toDto(TransactionCategory category) {
        TransactionCategoryDto dto = new TransactionCategoryDto();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setType(category.getType());
        dto.setTitle(category.getTitle());
        dto.setIcon(category.getIcon());

        return dto;
    }

    public static TransactionCategory fromDto(TransactionCategoryDto dto) {
        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setId(dto.getId());
        transactionCategory.setCode(dto.getCode());
        transactionCategory.setType(dto.getType());
        transactionCategory.setTitle(dto.getTitle());
        transactionCategory.setIcon(dto.getIcon());

        return transactionCategory;
    }

}
