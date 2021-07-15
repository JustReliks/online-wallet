package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.security.ChangePassRequestDto;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.exceptions.PasswordMatchException;
import ru.onlinewallet.service.UserService;



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
}
