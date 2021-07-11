package ru.onlinewallet.config.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.onlinewallet.entity.security.JwtUserDetails;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication, String jwtCreatedTimeHash) throws JOSEException {
        final LocalDateTime dateTime = LocalDateTime.now();

        Assert.notNull(authentication, "Authentication is missing");
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

        Assert.notNull(userDetails, "Authentication token is missing");

        //build claims
        SignedJWT signedJWT = getSignedJWT(userDetails, dateTime, jwtCreatedTimeHash);
        return signedJWT.serialize();
    }

    public String generateJwtToken(JwtUserDetails jwtUserDetails, String jwtCreatedTimeHash) throws JOSEException {
        final LocalDateTime dateTime = LocalDateTime.now();

        Assert.notNull(jwtUserDetails, "Authentication token is missing");

        //build claims
        SignedJWT signedJWT = getSignedJWT(jwtUserDetails, dateTime, jwtCreatedTimeHash);
        return signedJWT.serialize();
    }

    private SignedJWT getSignedJWT(JwtUserDetails jwtUserDetails, LocalDateTime dateTime, String jwtCreatedTimeHash) throws JOSEException {
        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
        jwtClaimsSetBuilder.expirationTime(Date.from(dateTime.plusMinutes(jwtExpiration)
                .atZone(ZoneId.systemDefault()).toInstant()));
        jwtClaimsSetBuilder.claim("username", jwtUserDetails.getUsername());
        jwtClaimsSetBuilder.claim("roles", getRoles(jwtUserDetails));
        jwtClaimsSetBuilder.claim("jwtCreatedTimeHash", jwtCreatedTimeHash);

        //signature
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSetBuilder.build());
        signedJWT.sign(new MACSigner(jwtSecret));
        return signedJWT;
    }

    public List<String> getRoles(JwtUserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String getUserNameFromJwtToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return String.valueOf(signedJWT.getJWTClaimsSet().getClaim("username"));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(authToken);
            boolean isVerified = signedJWT.verify(new MACVerifier(jwtSecret.getBytes()));

            if (!isVerified) {
                throw new BadCredentialsException("Invalid token signature");
            }

            //is token expired ?
            LocalDateTime expirationTime = LocalDateTime.ofInstant(
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), ZoneId.systemDefault());

            if (LocalDateTime.now(ZoneId.systemDefault()).isAfter(expirationTime)) {
                throw new CredentialsExpiredException("Token expired");
            }

            return true;
        } catch (ParseException e) {
            throw new InternalAuthenticationServiceException("Unreadable token");
        } catch (JOSEException e) {
            throw new InternalAuthenticationServiceException("Unreadable signature");
        }
    }

    public String getJwtCreatedTimeHashFromJwtToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return String.valueOf(signedJWT.getJWTClaimsSet().getClaim("jwtCreatedTimeHash"));
    }
}
