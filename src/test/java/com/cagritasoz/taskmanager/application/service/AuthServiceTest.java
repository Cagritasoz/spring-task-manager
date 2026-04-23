package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.model.UserWithToken;
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
public class AuthServiceTest {

    @Mock private AuthManagerPort authManagerPort;
    @Mock private JwtPort jwtPort;
    @Mock private PasswordEncoderPort passwordEncoderPort;
    @Mock private ReadUserPort readUserPort;
    @Mock private WriteUserPort writeUserPort;
    @Mock private LoggerPort loggerPort;
    @InjectMocks private AuthService authService;

    private User incomingUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        incomingUser = new User(null, "alice", "alice@example.com", "plain_pass", null);
        savedUser = new User(10L, "alice", "alice@example.com", "encoded_pass", Role.USER);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        when(readUserPort.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoderPort.encodePassword("plain_pass")).thenReturn("encoded_pass");
        when(writeUserPort.saveUser(incomingUser)).thenReturn(savedUser);
        when(jwtPort.generateToken(savedUser)).thenReturn("jwt_token");

        UserWithToken result = authService.registerUser(incomingUser);

        assertEquals(savedUser.getId(), result.getUser().getId());
        assertEquals("jwt_token", result.getToken());
        assertEquals(Role.USER, incomingUser.getRole()); // role forced to USER regardless of input
        verify(passwordEncoderPort).encodePassword("plain_pass");
        verify(writeUserPort).saveUser(incomingUser);
        verify(jwtPort).generateToken(savedUser);
    }

    @Test
    void shouldThrowEmailAlreadyExistsWhenEmailIsTaken() {
        when(readUserPort.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.registerUser(incomingUser));

        verify(passwordEncoderPort, never()).encodePassword(any());
        verify(writeUserPort, never()).saveUser(any());
        verify(jwtPort, never()).generateToken(any());
    }

    @Test
    void shouldLoginUserAndReturnTokenSuccessfully() {
        when(authManagerPort.authenticate("alice@example.com", "plain_pass")).thenReturn(savedUser);
        when(jwtPort.generateToken(savedUser)).thenReturn("jwt_token");

        UserWithToken result = authService.loginUser("alice@example.com", "plain_pass");

        assertEquals(savedUser.getId(), result.getUser().getId());
        assertEquals("jwt_token", result.getToken());
        verify(authManagerPort).authenticate("alice@example.com", "plain_pass");
        verify(jwtPort).generateToken(savedUser);
    }
}
