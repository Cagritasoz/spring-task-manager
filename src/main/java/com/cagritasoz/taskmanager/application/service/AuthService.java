package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.AuthenticatedUser;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthManagerPort authManagerPort;

    private final JwtPort jwtPort;

    private final PasswordEncoderPort passwordEncoderPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;


    @Override
    public AuthenticatedUser loginUser(String email, String password) {
        return null;
    }

    @Override
    public AuthenticatedUser registerUser(User user) {

        if(readUserPort.existsByEmail(user.getEmail())) {

            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " exists!");

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));
        user.setRole(Role.USER); //Public rest endpoint users are all users.

        User savedUser = writeUserPort.saveUser(user);

        String jwt = jwtPort.generateToken(savedUser);

        return new AuthenticatedUser(savedUser, jwt);

    }
}
