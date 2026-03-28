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

    private final LoggerPort loggerPort;


    @Override
    public UserWithToken loginUser(String email, String password) {

        User authenticatedUser = authManagerPort.authenticate(email, password);

        return new UserWithToken(authenticatedUser, generateToken(authenticatedUser));

    }

    @Override
    public UserWithToken registerUser(User user) {

        String newUserEmail = user.getEmail();

        loggerPort.logInfo("Register attempt. Email: {}",
                newUserEmail);

        if(readUserPort.existsByEmail(newUserEmail)) {

            loggerPort.logWarn("Register failed. Email: {}",
                    newUserEmail);

            throw new EmailAlreadyExistsException();

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));
        user.setRole(Role.USER); //Public rest endpoint users are all users.

        User savedUser = writeUserPort.saveUser(user);

        loggerPort.logInfo("Register successful. Email: {}",
                newUserEmail);

        return new UserWithToken(savedUser, generateToken(savedUser));

    }

    private String generateToken(User user) {

        loggerPort.logInfo("Generating JWT token. Email: {}",
                user.getEmail());

        return jwtPort.generateToken(user);

    }
}
