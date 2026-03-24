package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.domain.exception.UnauthorizedAccessException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    @Override
    public User getUser(Long id) {

        User currentUser = currentUserPort.getCurrentUser();

        if(currentUser.getRole() == Role.USER && !currentUser.getId().equals(id)) {

            throw new UnauthorizedAccessException();

        }

    }
}
