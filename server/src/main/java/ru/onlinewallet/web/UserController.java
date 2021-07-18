package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.onlinewallet.dto.UserSettingsDto;
import ru.onlinewallet.dto.security.ChangePassRequestDto;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.user.UserSettings;
import ru.onlinewallet.exceptions.PasswordMatchException;
import ru.onlinewallet.service.UserService;

import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/password")
    private ResponseEntity<JwtUserDetails> changePassword(@RequestBody ChangePassRequestDto changePassRequest) {
        try {
            return ResponseEntity.ok(this.userService.changePassword(changePassRequest));
        } catch (PasswordMatchException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    //User settings page
    @GetMapping("/settings")
    private ResponseEntity<UserSettingsDto> getUserProfile(@RequestParam("id") Long userId) {
        UserSettingsDto dto = UserSettingsDto.toDto(userService.getUserProfile(userId));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/settings")
    private ResponseEntity<UserSettingsDto> updateUserProfile(@RequestBody UserSettingsDto userSettingsDto) throws IOException {
        Long userId = userSettingsDto.getUserId();
        if (Objects.isNull(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userService.getUser(userId);
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        UserSettings userSettings = UserSettingsDto.fromDto(userSettingsDto);
        return ResponseEntity.ok(UserSettingsDto.toDto(userService.updateUserProfile(userSettings)));
    }

    @PostMapping(value = "/settings",consumes = {"multipart/form-data"})
    private ResponseEntity<UserSettingsDto> setUserPhoto(
            @RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        //UserSettings userSettings = UserSettingsDto.fromDto(userSettingsDto);
        if (Objects.isNull(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(UserSettingsDto.toDto(userService.saveProfileImage(userId, file)));
    }
}
