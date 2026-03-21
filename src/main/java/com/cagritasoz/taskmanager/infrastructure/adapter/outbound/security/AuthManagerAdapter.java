package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.AuthManagerPort;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManagerAdapter implements AuthManagerPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticate(String email, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        return securityUser.getUser();
    }
}
