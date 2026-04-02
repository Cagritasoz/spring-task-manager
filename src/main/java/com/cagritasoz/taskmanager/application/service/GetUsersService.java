package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUsersUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.handler.UserReadLogBuilder;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUsersService implements GetUsersUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final UserReadLogBuilder logBuilder;

    private static final Action action = Action.VIEW_LIST;

    @Override
    public Pagination<User> getUsers(Pagination<User> pagination) {

        User currentUser = currentUserPort.getCurrentUser();

        UserReadLogBuilder.ReadUsersContext context = logBuilder.createContext(currentUser);

        logBuilder.logListRetrievalAttempt(context, action);

        Pagination<User> usersPaginated = readUserPort.findAll(pagination);

        logBuilder.logListRetrievalSuccess(context, action);

        return usersPaginated;

    }
}
