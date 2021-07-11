package ru.onlinewallet.config.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.onlinewallet.entity.security.JwtUserDetails;
import ru.onlinewallet.exceptions.JwtCreatedTimeTokenMismatchedException;
import ru.onlinewallet.service.impl.JwtUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final JwtUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            String username;
            String jwtCreatedTime;
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                username = jwtUtils.getUserNameFromJwtToken(jwt);
                jwtCreatedTime = jwtUtils.getJwtCreatedTimeHashFromJwtToken(jwt);
                if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    JwtUserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (!jwtCreatedTime.equals(userDetails.getJwtCreatedTimeHash())) {
                        throw new JwtCreatedTimeTokenMismatchedException("The token generation time does not match. " +
                                "The user's password may have been changed.");
                    }

                    userDetails.setToken(jwt);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtCreatedTimeTokenMismatchedException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
