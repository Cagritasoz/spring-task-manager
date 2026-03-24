package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.PasswordEncoderPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final PasswordEncoderPort passwordEncoderPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;

    @Override
    public User createUser(User user) {

        if(readUserPort.existsByEmail(user.getEmail())) {

            throw new EmailAlreadyExistsException();

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));

        return writeUserPort.saveUser(user);

    }
}
