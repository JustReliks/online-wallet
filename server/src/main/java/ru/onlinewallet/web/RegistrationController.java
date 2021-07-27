package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.ResponseMessage;
import ru.onlinewallet.dto.UserRegistrationDto;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.user.UserSettings;
import ru.onlinewallet.service.RegistrationService;
import ru.onlinewallet.service.UserService;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseMessage> create(@RequestParam("generate-demo") boolean isGenerateDemo,
                                                  @RequestBody UserRegistrationDto dto) {
        String message;
        if (registrationService.checkUserExistByUserName(dto.getUsername())
                || registrationService.checkUserExistByUserEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = UserRegistrationDto.fromDto(dto);


        try {
            Long userId = registrationService.register(user);
            if (Objects.nonNull(userId)) {
                UserSettings userSettings = getUserSettings(dto, userId);
                userService.saveUserSettings(userSettings);
            }
            message = "New user created successfully, id: " + userId;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Error when creating a user! " + "(" + e.getMessage() + ")";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/exist")
    public ResponseEntity<Boolean> checkExist(@RequestParam(value = "username", required = false) String username,
                                              @RequestParam(value = "email", required = false) String email) {
        return Objects.nonNull(username)
                ? ResponseEntity.ok(registrationService.checkUserExistByUserName(username))
                : ResponseEntity.ok(registrationService.checkUserExistByUserEmail(email));
    }

    private UserSettings getUserSettings(UserRegistrationDto dto, Long userId) {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(userId);
        userSettings.setFirstName(dto.getFirstName());
        userSettings.setLastName(dto.getLastName());
        userSettings.setMiddleName(dto.getMiddleName());
        userSettings.setCurrency(dto.getCurrency());
        return userSettings;
    }
}
