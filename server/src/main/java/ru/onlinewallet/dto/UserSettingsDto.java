package ru.onlinewallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.user.UserSettings;

import java.util.Objects;

@Data
@NoArgsConstructor
public class UserSettingsDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String about;
    private String url;
    private String phone;
    private String country;
    private String language;
    private String currency;
    private byte[] profileImage;

    public static UserSettingsDto toDto(UserSettings userSettings) {
        UserSettingsDto userSettingsDto = new UserSettingsDto();
        userSettingsDto.setId(userSettings.getId());
        userSettingsDto.setUserId(userSettings.getUserId());
        userSettingsDto.setFirstName(userSettings.getFirstName());
        userSettingsDto.setLastName(userSettings.getLastName());
        userSettingsDto.setMiddleName(userSettings.getMiddleName());
        userSettingsDto.setAbout(userSettings.getAbout());
        userSettingsDto.setUrl(userSettings.getUrl());
        userSettingsDto.setPhone(userSettings.getPhone());
        userSettingsDto.setCountry(userSettings.getCountry());
        userSettingsDto.setLanguage(userSettings.getLanguage());
        userSettingsDto.setCurrency(userSettings.getCurrency());
        userSettingsDto.setProfileImage(userSettings.getProfileImage());

        return userSettingsDto;
    }

    public static UserSettings fromDto(UserSettingsDto userSettingsDto) {
        UserSettings userSettings = new UserSettings();
        Long id = userSettingsDto.getId();
        if (Objects.nonNull(id)) {
            userSettings.setId(id);
        }
        userSettings.setUserId(userSettingsDto.getUserId());
        userSettings.setFirstName(userSettingsDto.getFirstName());
        userSettings.setMiddleName(userSettingsDto.getMiddleName());
        userSettings.setLastName(userSettingsDto.getLastName());
        userSettings.setAbout(userSettingsDto.getAbout());
        userSettings.setUrl(userSettingsDto.getUrl());
        userSettings.setPhone(userSettingsDto.getPhone());
        userSettings.setCountry(userSettingsDto.getCountry());
        userSettings.setLanguage(userSettingsDto.getLanguage());
        userSettings.setCurrency(userSettingsDto.getCurrency());
        if (Objects.nonNull(userSettingsDto.getProfileImage())) {
            userSettings.setProfileImage(userSettingsDto.getProfileImage());
        }
        return userSettings;
    }
}
