package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.JwtPort;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final JwtUtils jwtUtils;

    @Override
    public String generateToken(User user) {

        return jwtUtils.generateToken(Map.of(), new SecurityUser(user));

    }
}
