package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.AuthManagerPort;
import com.cagritasoz.taskmanager.domain.exception.BadCredentialsException;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j //Logger
@Component
@RequiredArgsConstructor
public class AuthManagerAdapter implements AuthManagerPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticate(String email, String password) {

        try {

            log.info("Login attempt. Email: {}", email); //{} is a placeholder.

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email,
                    password);

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            log.info("Login successful. Email: {}", email);

            return securityUser.getUser();

        } catch (AuthenticationException e) { //Either UsernameNotFoundException or BadCredentialsException(Spring)

            log.info("Login failed. Reason: {}", e.getMessage());

            throw new BadCredentialsException();

        }
    }
}

