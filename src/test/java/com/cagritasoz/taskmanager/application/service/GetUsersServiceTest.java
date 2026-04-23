package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserReadLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetUsersServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private UserReadLogBuilder logBuilder;
    @InjectMocks private GetUsersService getUsersService;

    private User adminUser;
    private Pagination<User> paginationRequest;
    private Pagination<User> paginationResult;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        List<User> users = List.of(DataFactory.regularUser(), DataFactory.regularUserWithId(3L));
        paginationRequest = DataFactory.userPage(List.of());
        paginationResult = DataFactory.userPage(users);
    }

    @Test
    void shouldReturnPaginatedUsersSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.findAll(paginationRequest)).thenReturn(paginationResult);

        Pagination<User> result = getUsersService.getUsers(paginationRequest);

        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(logBuilder).logListRetrievalAttempt(null, Action.VIEW_LIST);
        verify(logBuilder).logListRetrievalSuccess(null, Action.VIEW_LIST);
    }
}
