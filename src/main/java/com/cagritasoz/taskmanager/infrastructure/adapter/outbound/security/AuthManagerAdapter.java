package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.AuthManagerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManagerAdapter implements AuthManagerPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticateUser(String email, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    }
}
