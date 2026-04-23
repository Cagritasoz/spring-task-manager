package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserModificationLogBuilder;
import com.cagritasoz.taskmanager.application.service.testutil.DataFactory;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
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
public class UpdateUserServiceTest {

    @Mock private PasswordEncoderPort passwordEncoderPort;
    @Mock private CurrentUserPort currentUserPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private WriteUserPort writeUserPort;
    @Mock private UserModificationLogBuilder logBuilder;
    @InjectMocks private UpdateUserService updateUserService;

    private User adminUser;
    private User regularUser;
    private User updatePayload;

    @BeforeEach
    void setUp() {
        adminUser = DataFactory.adminUser();
        regularUser = DataFactory.regularUser(); // id=2, role=USER
        updatePayload = new User(null, "updated_name", "updated@example.com", "new_pass", Role.USER);
    }

    // enforceRole calls context.currentUserRole(), so tests that reach it need a real (non-null) context.
    private UserModificationLogBuilder.ModifyUserContext contextFor(User caller, Long targetId) {
        return new UserModificationLogBuilder.ModifyUserContext(caller.getId(), caller.getRole(), targetId);
    }

    @Test
    void shouldUpdateOwnProfileSuccessfully() {
        UserModificationLogBuilder.ModifyUserContext ctx = contextFor(regularUser, 2L);
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(logBuilder.createContext(regularUser, 2L)).thenReturn(ctx);
        when(readUserPort.findById(2L)).thenReturn(Optional.of(regularUser));
        when(passwordEncoderPort.matches("new_pass", regularUser.getPassword())).thenReturn(false);
        when(passwordEncoderPort.encodePassword("new_pass")).thenReturn("encoded_new");
        User expectedResult = new User(2L, "updated_name", "updated@example.com", "encoded_new", Role.USER);
        when(writeUserPort.updateUser(2L, updatePayload)).thenReturn(expectedResult);

        User result = updateUserService.updateUser(2L, updatePayload);

        assertEquals(2L, result.getId());
        assertEquals("updated_name", result.getUsername());
        verify(logBuilder).logSuccess(ctx, Action.UPDATE);
        verify(logBuilder, never()).logForbidden(any(), any());
    }

    @Test
    void shouldAllowAdminToUpdateAnyUser() {
        UserModificationLogBuilder.ModifyUserContext ctx = contextFor(adminUser, 2L);
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(logBuilder.createContext(adminUser, 2L)).thenReturn(ctx);
        when(readUserPort.findById(2L)).thenReturn(Optional.of(regularUser));
        when(passwordEncoderPort.matches(any(), any())).thenReturn(true);
        when(passwordEncoderPort.encodePassword(any())).thenReturn("encoded_pass");
        when(writeUserPort.updateUser(2L, updatePayload)).thenReturn(regularUser);

        User result = updateUserService.updateUser(2L, updatePayload);

        assertNotNull(result);
        verify(writeUserPort).updateUser(2L, updatePayload);
        verify(logBuilder).logSuccess(ctx, Action.UPDATE);
    }

    @Test
    void shouldThrowForbiddenWhenUserUpdatesAnotherUsersProfile() {
        User differentUser = DataFactory.regularUserWithId(3L);
        when(currentUserPort.getCurrentUser()).thenReturn(differentUser);

        // No need to stub createContext — test throws before enforceRole is reached,
        // so the null context from the mock is never dereferenced.
        assertThrows(ForbiddenException.class,
                () -> updateUserService.updateUser(2L, updatePayload));

        verify(logBuilder).logForbidden(null, Action.UPDATE);
        verify(writeUserPort, never()).updateUser(any(), any());
    }

    @Test
    void shouldThrowUserNotFoundWhenTargetUserDoesNotExist() {
        when(currentUserPort.getCurrentUser()).thenReturn(adminUser);
        when(readUserPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> updateUserService.updateUser(99L, updatePayload));

        verify(logBuilder).logUserNotFound(null, Action.UPDATE);
        verify(writeUserPort, never()).updateUser(any(), any());
    }

    @Test
    void shouldResetRoleToUserWhenNonAdminTriesToEscalateToAdmin() {
        User maliciousPayload = new User(null, "user", "user@example.com", "pass", Role.ADMIN);
        UserModificationLogBuilder.ModifyUserContext ctx = contextFor(regularUser, 2L);
        when(currentUserPort.getCurrentUser()).thenReturn(regularUser);
        when(logBuilder.createContext(regularUser, 2L)).thenReturn(ctx);
        when(readUserPort.findById(2L)).thenReturn(Optional.of(regularUser));
        when(passwordEncoderPort.matches(any(), any())).thenReturn(false);
        when(passwordEncoderPort.encodePassword(any())).thenReturn("encoded_pass");
        when(writeUserPort.updateUser(2L, maliciousPayload)).thenReturn(regularUser);

        updateUserService.updateUser(2L, maliciousPayload);

        assertEquals(Role.USER, maliciousPayload.getRole()); // role silently reset to USER
        verify(logBuilder).logMaliciousRequest(ctx);
    }
}
