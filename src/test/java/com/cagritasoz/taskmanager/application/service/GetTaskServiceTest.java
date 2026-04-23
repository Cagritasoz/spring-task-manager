package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskReadLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.TaskNotFoundException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTaskServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private ReadTaskPort readTaskPort;
    @Mock private TaskReadLogBuilder logBuilder;
    @InjectMocks private GetTaskService getTaskService;

    private User adminUser;
    private User regularUser;
    private Task task;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
        task = DataFactory.taskWithId(10L, 2L);
    }

    @Test
    void shouldReturnTaskWhenOwnerRequests() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.findById(10L)).thenReturn(Optional.of(task));

        Task result = getTaskService.getTask(2L, 10L);

        assertEquals(task.getId(), result.getId());
        verify(logBuilder).logAccessGranted(null, Action.VIEW);
        verify(logBuilder).logSuccess(null, Action.VIEW);
    }

    @Test
    void shouldReturnTaskWhenAdminRequests() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.findById(10L)).thenReturn(Optional.of(task));

        Task result = getTaskService.getTask(2L, 10L);

        assertEquals(task.getId(), result.getId());
        verify(logBuilder).logSuccess(null, Action.VIEW);
    }

    @Test
    void shouldThrowForbiddenWhenUserRequestsAnotherUsersTask() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class, () -> getTaskService.getTask(2L, 10L));

        verify(logBuilder).logForbidden(null, Action.VIEW);
        verify(readTaskPort, never()).findById(any());
    }

    @Test
    void shouldThrowUserNotFoundWhenOwnerDoesNotExist() {
        // Admin has access but the target user doesn't exist
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> getTaskService.getTask(99L, 10L));

        verify(logBuilder).logUserNotFound(null, Action.VIEW);
        verify(readTaskPort, never()).findById(any());
    }

    @Test
    void shouldThrowTaskNotFoundWhenTaskDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> getTaskService.getTask(2L, 99L));

        verify(logBuilder).logTaskNotFound(null, Action.VIEW);
    }
}
