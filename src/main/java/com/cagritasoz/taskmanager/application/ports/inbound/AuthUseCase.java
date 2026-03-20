package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.AuthenticatedUser;
import com.cagritasoz.taskmanager.domain.model.User;

public interface AuthUseCase {

    AuthenticatedUser loginUser(String email, String password);

    AuthenticatedUser registerUser(User user);

}
