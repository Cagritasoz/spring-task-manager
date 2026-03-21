package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.JwtUser;
import com.cagritasoz.taskmanager.domain.model.User;

public interface AuthUseCase {

    JwtUser loginUser(String email, String password);

    JwtUser registerUser(User user);

}
