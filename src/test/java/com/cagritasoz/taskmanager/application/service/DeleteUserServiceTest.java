package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserModificationLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
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
public class DeleteUserServiceTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private WriteUserPort writeUserPort;
    @Mock private UserModificationLogBuilder logBuilder;
    @InjectMocks private DeleteUserService deleteUserService;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2
    }

    @Test
    void shouldDeleteOwnAccountSuccessfully() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.existsById(2L)).thenReturn(true);

        deleteUserService.deleteUser(2L);

        verify(writeUserPort).deleteUser(2L);
        verify(logBuilder).logSuccess(null, Action.DELETE);
        verify(logBuilder, never()).logForbidden(any(), any());
    }

    @Test
    void shouldAllowAdminToDeleteAnyUser() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(2L)).thenReturn(true);

        deleteUserService.deleteUser(2L);

        verify(writeUserPort).deleteUser(2L);
        verify(logBuilder).logSuccess(null, Action.DELETE);
    }

    @Test
    void shouldThrowForbiddenWhenUserDeletesAnotherUser() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class, () -> deleteUserService.deleteUser(2L));

        verify(logBuilder).logForbidden(null, Action.DELETE);
        verify(writeUserPort, never()).deleteUser(any());
    }

    @Test
    void shouldThrowUserNotFoundWhenUserDoesNotExist() {
        // Admin has access but target user doesn't exist
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> deleteUserService.deleteUser(99L));

        verify(logBuilder).logUserNotFound(null, Action.DELETE);
        verify(writeUserPort, never()).deleteUser(any());
    }
}
