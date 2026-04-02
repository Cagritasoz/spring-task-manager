package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetUserTest {

    @Mock
    private CurrentUserPort currentUserPort;

    @Mock
    private ReadUserPort readUserPort;

    @Mock
    private WriteUserPort writeUserPort;

    @InjectMocks
    private GetUserService getUserService;

    private User currentUser;
    private Long targetUserId;

    @BeforeEach
    void setUp() {



    }




}
