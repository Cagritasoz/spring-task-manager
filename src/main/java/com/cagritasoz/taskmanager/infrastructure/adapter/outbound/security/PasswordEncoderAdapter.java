package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String password) {

        return passwordEncoder.encode(password);

    }
}
