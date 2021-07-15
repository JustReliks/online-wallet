package ru.onlinewallet.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onlinewallet.dto.UserDtoLight;
import ru.onlinewallet.dto.security.ValidateCodeDto;
import ru.onlinewallet.entity.User;
import ru.onlinewallet.service.TwoFactorService;
import ru.onlinewallet.service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class TwoFactorController {

    private final GoogleAuthenticator gAuth;
    private final UserService userService;
    private final TwoFactorService twoFactorService;


    @GetMapping(value = "/generate/{username}")
    public ResponseEntity<String> generate(@RequestParam(value = "regenerate", required = false) boolean regenerate,
                                           @PathVariable String username) throws WriterException, IOException {
        if (regenerate) {
            twoFactorService.setNeedRegenerateState(username, true);
            User user = userService.getUser(username);
            user.setIsTwoFactorEnabled(false);
            userService.update(user);
        }
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Online-Wallet.ru", username
                , key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
        return ResponseEntity.ok(Base64.getEncoder().encodeToString(bos.toByteArray()));
    }

    @PostMapping("/validate/key")
    public boolean validateKey(@RequestBody ValidateCodeDto body) {
        return gAuth.authorizeUser(body.getUsername(), body.getCode());
    }

    @GetMapping("/two-factor/{user}")
    private ResponseEntity<UserDtoLight> changeTwoFactorState(@PathVariable("user") String user,
                                                              @RequestParam("action") String action) {
        return ResponseEntity.ok(UserDtoLight.toDto(userService.changeTwoFactorState(user, action)));
    }
}
