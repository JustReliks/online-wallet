package ru.onlinewallet.service.impl;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.onlinewallet.config.security.jwt.JwtUtils;
import ru.onlinewallet.dto.security.ChangePassRequestDto;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.user.UserSettings;
import ru.onlinewallet.exceptions.PasswordMatchException;
import ru.onlinewallet.repo.user.RoleRepository;
import ru.onlinewallet.repo.user.UserRepository;
import ru.onlinewallet.repo.user.UserSettingsRepository;
import ru.onlinewallet.service.UserService;
import ru.onlinewallet.util.PasswordUtil;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final UserSettingsRepository userSettingsRepository;

    @Override
    @Transactional
    public Long save(User user) {
        user.setBalance(0D);
        user.setAccessToken("access_token");
        user.setCreatedAt(Instant.now());
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName("user")));
        user.setIsTwoFactorEnabled(false);
        return userRepository.save(user).getId();
    }

    @Override
    public JwtUserDetails authenticate(String username, String password) throws JOSEException {
        String jwt;
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = getUser(username);
        String jwtCreatedTimeHash = user.getJwtCreatedTimeHash();
        if (Objects.isNull(jwtCreatedTimeHash)) {
            jwtCreatedTimeHash = DigestUtils.md5Hex(LocalDateTime.now().toString());
            user.setJwtCreatedTimeHash(jwtCreatedTimeHash);
            update(user);
        }
        jwt = jwtUtils.generateJwtToken(auth, jwtCreatedTimeHash);
        ((JwtUserDetails) auth.getPrincipal()).setToken(jwt);
        return (JwtUserDetails) auth.getPrincipal();
    }

    private long getExpire(LocalDateTime expirationDate) {
        return expirationDate.atZone(ZoneId.of("Europe/Moscow")).toEpochSecond();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id +
                " " +
                "not found!"));
    }

    @Override
    public User getUser(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new EntityNotFoundException("???????????????????????? " + userName + " ???? ????????????!"));
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User changeTwoFactorState(String username, String action) {
        User user = getUser(username);
        if (action.equals("on")) {
            user.setIsTwoFactorEnabled(true);
        } else if (action.equals("off")) {
            user.setIsTwoFactorEnabled(false);
        } else {
            throw new RuntimeException("State not recognized");
        }
        return update(user);
    }

    @Override
    public JwtUserDetails changePassword(ChangePassRequestDto changePassRequest) throws JOSEException {
        User user = getUser(changePassRequest.getUserId());
        final JwtUserDetails userDetails = jwtUserDetailsService.getJwtUserDetailsFromUser(user);
        String oldPass = PasswordUtil.decodeBtoaPassword(changePassRequest.getOldPass());
        if (!bCryptPasswordEncoder.matches(oldPass, user.getPassword())) {
            throw new PasswordMatchException();
        }
        String newPassword = PasswordUtil.decodeBtoaPassword(changePassRequest.getNewPass());
        String newConfirmPassword = PasswordUtil.decodeBtoaPassword(changePassRequest.getConfirmNewPass());
        if (!newPassword.equals(newConfirmPassword)) {
            throw new RuntimeException("???????????? ???? ??????????????????");
        }
        changePassword(user, userDetails, newPassword);
        return userDetails;
    }

    @Override
    public User changePassword(User user, String password) throws JOSEException {
        final JwtUserDetails userDetails = jwtUserDetailsService.getJwtUserDetailsFromUser(user);
        changePassword(user, userDetails, password);
        return user;
    }

    @Override
    public UserSettings getUserProfile(Long userId) {
        UserSettings userSettings = userSettingsRepository.findByUserId(userId);
        if (Objects.isNull(userSettings)) {
            throw new EntityNotFoundException("?????????????? ???????????????????????? ???? ????????????!");
        }
        return userSettings;
    }

    @Override
    public UserSettings updateUserProfile(UserSettings userSettings) {

        if (Objects.isNull(userSettings.getId())) {
            throw new EntityNotFoundException("???????????????????????????????? ???????????????? ???? ????????????????????!");
        }

        return saveUserSettings(userSettings);
    }

    @Override
    @Transactional
    public UserSettings saveUserSettings(UserSettings userSettings) {
        if (Objects.isNull(userSettings.getUserId())) {
            throw new EntityNotFoundException("???????????? ???????????????????????? ???? ????????????????????!");
        }
        return userSettingsRepository.save(userSettings);
    }

    @Override
    public Long register(User user) {
        return save(user);
    }

    @Override
    @Transactional
    public UserSettings saveProfileImage(long userId, MultipartFile file) throws IOException {
        UserSettings byUserId = userSettingsRepository.findByUserId(userId);
        byUserId.setProfileImage(file.getBytes());
        return userSettingsRepository.save(byUserId);
    }

    private UserSettings createUserSettings(Long save) {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(save);
        return userSettings;
    }

    private void changePassword(User user, JwtUserDetails userDetails, String newPassword) throws JOSEException {
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        String jwtCreatedTimeHash = DigestUtils.md5Hex(LocalDateTime.now().toString());
        String token = jwtUtils.generateJwtToken(userDetails, jwtCreatedTimeHash);
        user.setJwtCreatedTimeHash(jwtCreatedTimeHash);
        userDetails.setToken(token);
        update(user);
    }
}


