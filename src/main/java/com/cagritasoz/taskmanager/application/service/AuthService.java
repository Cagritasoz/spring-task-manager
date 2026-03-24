package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.model.UserWithToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthManagerPort authManagerPort;

    private final JwtPort jwtPort;

    private final PasswordEncoderPort passwordEncoderPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;


    @Override
    public UserWithToken loginUser(String email, String password) {

        User authenticatedUser = authManagerPort.authenticate(email, password);

        return new UserWithToken(authenticatedUser, generateToken(authenticatedUser));

    }

    @Override
    public UserWithToken registerUser(User user) {

        if(readUserPort.existsByEmail(user.getEmail())) {

            throw new EmailAlreadyExistsException("Email exists");

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));
        user.setRole(Role.USER); //Public rest endpoint users are all users.

        User savedUser = writeUserPort.saveUser(user);

        return new UserWithToken(savedUser, generateToken(savedUser));

    }

    private String generateToken(User user) {

        return jwtPort.generateToken(user);

    }
}
