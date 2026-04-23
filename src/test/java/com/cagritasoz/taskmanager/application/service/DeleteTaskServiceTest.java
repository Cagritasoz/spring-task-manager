package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskModificationLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.TaskNotFoundException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTaskServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private ReadTaskPort readTaskPort;
    @Mock private WriteTaskPort writeTaskPort;
    @Mock private TaskModificationLogBuilder logBuilder;
    @InjectMocks private DeleteTaskService deleteTaskService;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
    }

    @Test
    void shouldDeleteOwnTaskSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(10L)).thenReturn(true);

        deleteTaskService.deleteTask(2L, 10L);

        verify(writeTaskPort).deleteTask(10L);
        verify(logBuilder).logSuccess(null, Action.DELETE);
        verify(logBuilder, never()).logForbidden(any(), any());
    }

    @Test
    void shouldAllowAdminToDeleteAnyTask() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(10L)).thenReturn(true);

        deleteTaskService.deleteTask(2L, 10L);

        verify(writeTaskPort).deleteTask(10L);
        verify(logBuilder).logSuccess(null, Action.DELETE);
    }

    @Test
    void shouldThrowForbiddenWhenUserDeletesAnotherUsersTask() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class, () -> deleteTaskService.deleteTask(2L, 10L));

        verify(logBuilder).logForbidden(null, Action.DELETE);
        verify(writeTaskPort, never()).deleteTask(any());
    }

    @Test
    void shouldThrowUserNotFoundWhenTargetUserDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> deleteTaskService.deleteTask(99L, 10L));

        verify(logBuilder).logUserNotFound(null, Action.DELETE);
        verify(writeTaskPort, never()).deleteTask(any());
    }

    @Test
    void shouldThrowTaskNotFoundWhenTaskDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> deleteTaskService.deleteTask(2L, 99L));

        verify(logBuilder).logTaskNotFound(null, Action.DELETE);
        verify(writeTaskPort, never()).deleteTask(any());
    }
}
