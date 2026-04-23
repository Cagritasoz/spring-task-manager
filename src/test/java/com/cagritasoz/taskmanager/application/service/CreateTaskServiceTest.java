package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskCreationLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTaskServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private WriteTaskPort writeTaskPort;
    @Mock private TaskCreationLogBuilder logBuilder;
    @InjectMocks private CreateTaskService createTaskService;

    private User adminUser;
    private User regularUser;
    private Task incomingTask;
    private Task savedTask;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
        incomingTask = DataFactory.unsavedTask(2L);
        savedTask = DataFactory.taskWithId(1L, 2L);
    }

    @Test
    void shouldCreateTaskForOwnAccountSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(writeTaskPort.saveTask(2L, incomingTask)).thenReturn(savedTask);

        Task result = createTaskService.createTask(2L, incomingTask);

        assertEquals(savedTask.getId(), result.getId());
        assertEquals(2L, result.getUserId());
        verify(logBuilder).logAccessGranted(null, Action.CREATE);
        verify(logBuilder).logSuccess(null, Action.CREATE);
    }

    @Test
    void shouldAllowAdminToCreateTaskForAnyUser() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(writeTaskPort.saveTask(2L, incomingTask)).thenReturn(savedTask);

        Task result = createTaskService.createTask(2L, incomingTask);

        assertNotNull(result);
        verify(writeTaskPort).saveTask(2L, incomingTask);
    }

    @Test
    void shouldThrowForbiddenWhenUserCreatesTaskForAnotherUser() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class,
                () -> createTaskService.createTask(2L, incomingTask));

        verify(logBuilder).logForbidden(null, Action.CREATE);
        verify(writeTaskPort, never()).saveTask(any(), any());
    }

    @Test
    void shouldThrowUserNotFoundWhenAdminCreatesTaskForNonExistentUser() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> createTaskService.createTask(99L, incomingTask));

        verify(logBuilder).logUserNotFound(null, Action.CREATE);
        verify(writeTaskPort, never()).saveTask(any(), any());
    }
}
