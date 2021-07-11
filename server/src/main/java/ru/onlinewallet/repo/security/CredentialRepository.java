package ru.onlinewallet.repo.security;

import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.onlinewallet.entity.security.UserTOTP;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CredentialRepository implements ICredentialRepository {

    private final UserTotpRepository userTotpRepository;

    @Override
    public String getSecretKey(String userName) {
        return getUser(userName).getSecretKey();
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        UserTOTP user = getUser(userName);
        if (Objects.isNull(user)) {
            user = new UserTOTP(null, userName, secretKey, validationCode,
                    scratchCodes.stream().mapToInt(Integer::intValue).toArray(), false);
        } else if (user.getNeedRegenerate()) {
            user.setValidationCode(validationCode);
            user.setSecretKey(secretKey);
            user.setScratchCodes(scratchCodes.stream().mapToInt(Integer::intValue).toArray());
            user.setNeedRegenerate(false);
        } else {
            user.setScratchCodes(scratchCodes.stream().mapToInt(Integer::intValue).toArray());
        }
        userTotpRepository.save(user);
    }

    public UserTOTP getUser(String username) {
        return userTotpRepository.findByUsername(username);
    }

}
