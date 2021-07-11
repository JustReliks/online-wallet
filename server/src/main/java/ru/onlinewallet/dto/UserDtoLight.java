package ru.onlinewallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.User;
import ru.onlinewallet.entity.security.JwtUserDetails;


import java.time.Instant;

@Data
@NoArgsConstructor
public class UserDtoLight {
    private Long id;
    private String username;
    private String email;
    private double balance;
    private double bonuses;
    private int cases;
    private String uuid;
    private Instant createdAt;
    private boolean isTwoFactorEnabled;


    public UserDtoLight(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.uuid = user.getUuid();
        this.createdAt = user.getCreatedAt();
        this.isTwoFactorEnabled = user.getIsTwoFactorEnabled();
    }

    public UserDtoLight(JwtUserDetails userDetails) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.email = userDetails.getEmail();
        this.balance = userDetails.getBalance();
        this.uuid = userDetails.getUuid();
        this.createdAt = userDetails.getCreatedAt();
        this.bonuses = userDetails.getBonuses();
        this.cases = userDetails.getCases();
        this.isTwoFactorEnabled = userDetails.isTwoFactorEnabled();
    }

    public static UserDtoLight toDto(User user) {
        UserDtoLight userDtoLight = new UserDtoLight();
        userDtoLight.setId(user.getId());
        userDtoLight.setUsername(user.getUsername());
        userDtoLight.setEmail(user.getEmail());
        userDtoLight.setBalance(user.getBalance());
        userDtoLight.setUuid(user.getUuid());
        userDtoLight.setCreatedAt(user.getCreatedAt());
        userDtoLight.setTwoFactorEnabled(user.getIsTwoFactorEnabled());

        return userDtoLight;
    }

    @Data
    public static class UserPreAuthDto {
        private String username;

        public static UserPreAuthDto toDto(JwtUserDetails jwtUserDetails) {
            UserPreAuthDto userPreAuthDto = new UserPreAuthDto();
            userPreAuthDto.setUsername(jwtUserDetails.getUsername());
            return userPreAuthDto;
        }
    }
}
