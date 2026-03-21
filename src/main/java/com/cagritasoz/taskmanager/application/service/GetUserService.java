package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserCase;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserCase {

    private final ReadUserPort readUserPort;

    @Override
    public User getUser(Long id) {

        return readUserPort.findById(id)
                .orElseThrow((() -> new UserNotFoundException("User with " + id + " is not found!")));

    }
}
