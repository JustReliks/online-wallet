package ru.onlinewallet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import ru.onlinewallet.entity.User;
import ru.onlinewallet.entity.security.JwtUserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDtoBase extends UserDtoLight {
    private List<UserRoleDto> roles = new ArrayList<>();

    public UserDtoBase(User user) {
        super(user);

        if (!CollectionUtils.isEmpty(user.getRoles()))
            this.roles = user.getRoles().stream().map(UserRoleDto::new).collect(Collectors.toList());
    }

    public UserDtoBase(JwtUserDetails userDetails) {
        super(userDetails);

        if (!CollectionUtils.isEmpty(userDetails.getAuthorities()))
            this.roles = userDetails.getAuthorities().stream()
                    .map(UserRoleDto::new)
                    .collect(Collectors.toList());
    }
}
