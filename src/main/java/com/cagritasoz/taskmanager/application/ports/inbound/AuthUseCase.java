package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.model.UserWithToken;

public interface AuthUseCase {

    UserWithToken loginUser(String email, String password);

    UserWithToken registerUser(User user);

}
