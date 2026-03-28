package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface UpdateUserUseCase {

    User updateUser(Long id, User user);
}
