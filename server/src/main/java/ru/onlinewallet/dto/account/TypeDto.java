package ru.onlinewallet.dto.account;

import lombok.Data;
import ru.onlinewallet.entity.account.Type;

@Data
public class TypeDto {
    private Long id;
    private String name;
    private byte[] icon;

    public static TypeDto toDto(Type type) {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(type.getId());
        typeDto.setName(type.getName());
        typeDto.setIcon(type.getIcon());

        return typeDto;
    }
}
