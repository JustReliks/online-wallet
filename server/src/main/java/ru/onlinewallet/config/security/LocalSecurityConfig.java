package ru.onlinewallet.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.onlinewallet.config.security.jwt.JwtAuthenticationEntryPoint;
import ru.onlinewallet.config.security.jwt.JwtAuthenticationFilter;
import ru.onlinewallet.config.security.jwt.JwtUtils;
import ru.onlinewallet.service.impl.JwtUserDetailsService;


@Configuration
//@Profile("local")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class LocalSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;
    private final JwtUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout().permitAll().and()
                .authorizeRequests()
                .antMatchers("/api/registration").not().fullyAuthenticated()
                .antMatchers("/api/registration/exist/**",
                        "/api/restore-access").not().fullyAuthenticated()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(
                        "/auth/**",
                        "/api/dictionary/currencies",
                        "/api/code/validate/key"
                ).permitAll()
                .antMatchers("/auth/current").authenticated()
                .antMatchers("/api/users/**").hasRole("ADMIN")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().fullyAuthenticated()
                .and().headers().frameOptions().disable()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
