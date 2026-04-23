package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserReadLogBuilder;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetUserTest {

    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private UserReadLogBuilder logBuilder;
    @InjectMocks private GetUserService getUserService;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id = 2
    }

    @Test
    void shouldReturnUserWhenAdminRequestsAnotherUser() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.findById(2L)).thenReturn(Optional.of(regularUser));

        User result = getUserService.getUser(2L);

        assertEquals(regularUser.getId(), result.getId());
        verify(logBuilder).logAccessGranted(null, Action.VIEW);
        verify(logBuilder).logSuccess(null, Action.VIEW);
        verify(logBuilder, never()).logForbidden(any(), any());
    }

    @Test
    void shouldReturnUserWhenOwnerRequestsOwnProfile() {
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(readUserPort.findById(2L)).thenReturn(Optional.of(regularUser));

        User result = getUserService.getUser(2L);

        assertEquals(regularUser.getId(), result.getId());
        verify(logBuilder).logSuccess(null, Action.VIEW);
        verify(logBuilder, never()).logForbidden(any(), any());
    }

    @Test
    void shouldThrowForbiddenWhenUserRequestsAnotherUsersProfile() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        assertThrows(ForbiddenException.class, () -> getUserService.getUser(2L));

        verify(logBuilder).logForbidden(null, Action.VIEW);
        verify(readUserPort, never()).findById(any());
        verify(logBuilder, never()).logSuccess(any(), any());
    }

    @Test
    void shouldThrowUserNotFoundWhenAdminRequestsNonExistentUser() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserService.getUser(99L));

        verify(logBuilder).logUserNotFound(null, Action.VIEW);
        verify(logBuilder, never()).logSuccess(any(), any());
    }
}
