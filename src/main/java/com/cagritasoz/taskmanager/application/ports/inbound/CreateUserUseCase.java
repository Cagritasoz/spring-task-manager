package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface CreateUserUseCase {

    User createUser(User user);

}
