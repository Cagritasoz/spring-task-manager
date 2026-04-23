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
public class UpdateTaskServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private ReadTaskPort readTaskPort;
    @Mock private WriteTaskPort writeTaskPort;
    @Mock private TaskModificationLogBuilder logBuilder;
    @InjectMocks private UpdateTaskService updateTaskService;

    private User adminUser;
    private User regularUser;
    private Task updatePayload;
    private Task updatedTask;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
        updatePayload = DataFactory.unsavedTask(2L);
        updatedTask = DataFactory.taskWithId(10L, 2L);
    }

    @Test
    void shouldUpdateOwnTaskSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(10L)).thenReturn(true);
        when(writeTaskPort.updateTask(2L, 10L, updatePayload)).thenReturn(updatedTask);

        Task result = updateTaskService.updateTask(2L, 10L, updatePayload);

        assertEquals(updatedTask.getId(), result.getId());
        verify(logBuilder).logAccessGranted(null, Action.UPDATE);
        verify(logBuilder).logSuccess(null, Action.UPDATE);
    }

    @Test
    void shouldAllowAdminToUpdateAnyTask() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(10L)).thenReturn(true);
        when(writeTaskPort.updateTask(2L, 10L, updatePayload)).thenReturn(updatedTask);

        Task result = updateTaskService.updateTask(2L, 10L, updatePayload);

        assertNotNull(result);
        verify(writeTaskPort).updateTask(2L, 10L, updatePayload);
    }

    @Test
    void shouldThrowForbiddenWhenUserUpdatesAnotherUsersTask() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class,
                () -> updateTaskService.updateTask(2L, 10L, updatePayload));

        verify(logBuilder).logForbidden(null, Action.UPDATE);
        verify(writeTaskPort, never()).updateTask(any(), any(), any());
    }

    @Test
    void shouldThrowUserNotFoundWhenTargetUserDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> updateTaskService.updateTask(99L, 10L, updatePayload));

        verify(logBuilder).logUserNotFound(null, Action.UPDATE);
        verify(writeTaskPort, never()).updateTask(any(), any(), any());
    }

    @Test
    void shouldThrowTaskNotFoundWhenTaskDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);
        when(readTaskPort.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class,
                () -> updateTaskService.updateTask(2L, 99L, updatePayload));

        verify(logBuilder).logTaskNotFound(null, Action.UPDATE);
        verify(writeTaskPort, never()).updateTask(any(), any(), any());
    }
}
