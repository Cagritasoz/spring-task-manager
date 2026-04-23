package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskReadLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTasksServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private ReadTaskPort readTaskPort;
    @Mock private TaskReadLogBuilder logBuilder;
    @InjectMocks private GetTasksService getTasksService;

    private User adminUser;
    private User regularUser;
    private Pagination<Task> paginationRequest;
    private Pagination<Task> paginationResult;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
        paginationRequest = DataFactory.taskPage(List.of());
        List<Task> tasks = List.of(DataFactory.taskWithId(1L, 2L), DataFactory.taskWithId(2L, 2L));
        paginationResult = DataFactory.taskPage(tasks);
    }

    @Test
    void shouldReturnOwnTasksSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.findAll(2L, paginationRequest)).thenReturn(paginationResult);

        Pagination<Task> result = getTasksService.getTasks(2L, paginationRequest);

        assertEquals(2, result.getContent().size());
        verify(logBuilder).logListRetrievalAttempt(null, Action.VIEW_LIST);
        verify(logBuilder).logAccessGrantedForListRetrieval(null, Action.VIEW_LIST);
        verify(logBuilder).logSuccessForListRetrieval(null, Action.VIEW_LIST);
    }

    @Test
    void shouldAllowAdminToGetAnyUsersTasks() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.findAll(2L, paginationRequest)).thenReturn(paginationResult);

        Pagination<Task> result = getTasksService.getTasks(2L, paginationRequest);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void shouldThrowForbiddenWhenUserRequestsAnotherUsersTasks() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class,
                () -> getTasksService.getTasks(2L, paginationRequest));

        verify(logBuilder).logForbiddenForListRetrieval(null, Action.VIEW_LIST);
        verify(readTaskPort, never()).findAll(any(), any());
    }

    @Test
    void shouldThrowUserNotFoundWhenTargetUserDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> getTasksService.getTasks(99L, paginationRequest));

        verify(logBuilder).logUserNotFoundForListRetrieval(null, Action.VIEW_LIST);
        verify(readTaskPort, never()).findAll(any(), any());
    }
}
