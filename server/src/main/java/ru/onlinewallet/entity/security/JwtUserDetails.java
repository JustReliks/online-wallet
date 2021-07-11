package ru.onlinewallet.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.onlinewallet.entity.User;


import java.time.Instant;
import java.util.*;

@Data
public class JwtUserDetails implements UserDetails {

    @JsonIgnore
    private long id;
    private String username;
    private String password;
    private String email;
    private String token;
    private double balance;
    private String uuid;
    private Instant createdAt;
    private boolean active;
    private double bonuses;
    private int cases;
    private boolean isTwoFactorEnabled;
    private String jwtCreatedTimeHash;
    private List<GrantedAuthority> authorities;
    private Map<String, List<String>> rolePermissions = new HashMap<>();

    public JwtUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.active = true;
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.uuid = user.getUuid();
        this.createdAt = user.getCreatedAt();
        this.isTwoFactorEnabled = user.getIsTwoFactorEnabled();
        this.authorities = new ArrayList<>();

    }

    public JwtUserDetails(User user, List<GrantedAuthority> authorities, Map<String, List<String>> permissions) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.active = true;
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.authorities = authorities;
        this.balance = user.getBalance();
        this.uuid = user.getUuid();
        this.createdAt = user.getCreatedAt();
        this.isTwoFactorEnabled = user.getIsTwoFactorEnabled();
        this.jwtCreatedTimeHash = user.getJwtCreatedTimeHash();
        this.rolePermissions = permissions;

    }

    public JwtUserDetails(User user, List<GrantedAuthority> authorities, Map<String, List<String>> permissions,
                          String token) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.active = true;
        this.password = user.getPassword();
        this.rolePermissions = permissions;
        this.token = token;
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.uuid = user.getUuid();
        this.createdAt = user.getCreatedAt();
        this.isTwoFactorEnabled = user.getIsTwoFactorEnabled();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
