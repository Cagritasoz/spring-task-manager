package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.PasswordEncoderPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserCreationLogBuilder;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private CurrentUserPort currentUserPort;
    @Mock
    private ReadUserPort readUserPort;
    @Mock
    private WriteUserPort writeUserPort;
    @Mock
    private UserCreationLogBuilder logBuilder;

    @InjectMocks
    private CreateUserService createUserService;

    private User currentUser;
    private User newUser;
    private String newUserEmail;
    private User savedUser;
    private UserCreationLogBuilder.CreateUserContext context;

    @BeforeEach
    void setUp() {

        currentUser = new User(1L, "admin", "admin@example.com", "admin123", Role.ADMIN);
        newUser = new User(null, "newuser", "new@example.com", "new123", Role.USER);
        newUserEmail = newUser.getEmail();
        savedUser = new User(2L, "newuser", "new@example.com", "encodednew123", Role.USER);

        context = logBuilder.createContext(currentUser, newUserEmail);


    }

    @Test
    void shouldThrowEmailAlreadyExistsException() {

        when(currentUserPort.getCurrentUser()).thenReturn(currentUser); //Define method return types. Called stubbing.
        when(logBuilder.createContext(currentUser, newUserEmail)).thenReturn(context);
        when(readUserPort.existsByEmail(newUserEmail)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> createUserService.createUser(newUser));

        verify(currentUserPort).getCurrentUser();
        verify(logBuilder).logAttempt(context, Action.CREATE); //Verify that these methods were called exactly once.
        verify(readUserPort).existsByEmail(newUserEmail);
        verify(logBuilder).logEmailAlreadyExists(context, Action.CREATE);

        verify(passwordEncoderPort, never()).encodePassword("new123");
        verify(writeUserPort, never()).saveUser(newUser);
        verify(logBuilder, never()).logSuccess(context, Action.CREATE);

    }

    @Test
    void shouldCreateUserSuccessfully() {

        when(currentUserPort.getCurrentUser()).thenReturn(currentUser);
        when(logBuilder.createContext(currentUser, newUserEmail)).thenReturn(context);
        when(readUserPort.existsByEmail(newUserEmail)).thenReturn(false);
        when(passwordEncoderPort.encodePassword("new123")).thenReturn("encodednew123");
        when(writeUserPort.saveUser(newUser)).thenReturn(savedUser);

        User result = createUserService.createUser(newUser);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getPassword(), result.getPassword());

        verify(currentUserPort).getCurrentUser();
        verify(logBuilder).logAttempt(context, Action.CREATE); //Verify that these methods were called exactly once with these values .
        verify(readUserPort).existsByEmail(newUserEmail);
        verify(passwordEncoderPort).encodePassword("new123");
        verify(writeUserPort).saveUser(newUser);
        verify(logBuilder).logSuccess(context, Action.CREATE);

        verify(logBuilder, never()).logEmailAlreadyExists(context, Action.CREATE);


    }


}
