package com.cagritasoz.taskmanager.infrastructure.config;

import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security.SecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ReadUserPort readUserPort;

    @Bean
    public UserDetailsService userDetailsService() {

        return email -> new SecurityUser(readUserPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found.")));

    }

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        //mask UsernameNotFoundException as a BadCredentialsException. No one can check if an email exists in the system.
        authProvider.setHideUserNotFoundExceptions(true);

        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    /*  Spring Boot automatically collects all beans of type:
        AuthenticationProvider
        and registers them inside AuthenticationManager.

        AuthenticationManager.authenticate method uses the
        Authentication Provider registered inside AuthenticationManager
        which in this case is the DaoAuthenticationProvider that we create
        by passing the UserDetailsService which fetches the user from the database
        with the email that we extract from the jwt token as a subclaim(username).

        We have to set the password encoder too because in the login process the user sent
        password is then checked if it matches the hashed password of the loaded user through UserDetailsService.

        AuthenticationManager
                  ↓
       DaoAuthenticationProvider
                  ↓
   UserDetailsService.loadUserByUsername(email)
                  ↓
PasswordEncoder.matches(rawPassword, encodedPasswordFromDB)
                  ↓
If OK → return authenticated Authentication
If wrong → throw AuthenticationException(Bad Credentials extends AuthenticationException)


     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }
}
