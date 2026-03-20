package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface JwtPort {

    String generateToken(User user);

}
