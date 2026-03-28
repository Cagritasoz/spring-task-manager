package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface GetUserUseCase {

    User getUser(Long id);

}
