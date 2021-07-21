package ru.onlinewallet.dto.account;

import lombok.Data;
import ru.onlinewallet.entity.account.Type;

@Data
public class TypeDto {
    private Long id;
    private String name;
    private String code;
    private byte[] icon;

    public static TypeDto toDto(Type type) {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(type.getId());
        typeDto.setName(type.getName());
        typeDto.setCode(type.getCode());
        typeDto.setIcon(type.getIcon());

        return typeDto;
    }

    public static Type fromDto(TypeDto typeDto) {
        Type type = new Type();
        type.setId(typeDto.getId());
        type.setName(typeDto.getName());
        type.setCode(typeDto.getCode());
        type.setIcon(typeDto.getIcon());

        return type;

    }
}
