package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUsersUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUsersService implements GetUsersUseCase {

    private final ReadUserPort readUserPort;

    @Override
    public Pagination<User> getUsers(Pagination<User> pagination) {

        return readUserPort.findAll(pagination);

    }
}
