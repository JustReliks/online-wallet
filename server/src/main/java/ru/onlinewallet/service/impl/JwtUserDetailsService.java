package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.user.User;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.repo.user.UserRepository;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final static String ROLE_ADMIN = "ROLE_ADMIN";
    private final static String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;

    @Autowired
    private final HttpServletRequest httpServletRequest;

    @Override
    @Transactional
    public JwtUserDetails loadUserByUsername(String username) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User \"" + username + "\" not found"));
        return getJwtUserDetails(user);
    }

    private JwtUserDetails getJwtUserDetails(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, List<String>> permissions = new HashMap<>();
        user.getRoles().forEach(ur -> {
            String r = ur.getName();
            authorities.add(new SimpleGrantedAuthority(r));
            permissions.put("ROLE_USER", Collections.emptyList());
        });
        return new JwtUserDetails(user, authorities, permissions);
    }

    @Transactional
    public JwtUserDetails getJwtUserDetailsFromUser(User user) {
        return getJwtUserDetails(user);
    }

    public JwtUserDetails getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JwtUserDetails) {
            return (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else return null;
    }

    public Set<String> getUserPermissions() {
        JwtUserDetails userDetails = this.getCurrentUser();
        return userDetails.getRolePermissions().values().stream().flatMap(List::stream).collect(Collectors.toSet());
    }

    public boolean hasRole(String roleName) {
        JwtUserDetails userDetails = this.getCurrentUser();
        return userDetails.getRolePermissions().keySet().contains(roleName);
    }

    public boolean isAdmin() {
        JwtUserDetails userDetails = this.getCurrentUser();
        return userDetails.getRolePermissions().keySet().contains(ROLE_ADMIN);
    }

    @ManagedBean
    @EnableJpaAuditing
    public static class SpringSecurityAuditorAware implements AuditorAware<String> {

        private final HttpServletRequest httpServletRequest;

        public SpringSecurityAuditorAware(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
        }

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.ofNullable(httpServletRequest.getUserPrincipal())
                    .map(Principal::getName);
        }
    }

}
