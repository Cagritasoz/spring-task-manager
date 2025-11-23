package com.cagritasoz.taskmanager.domain.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface UserUseCase {

    User registerUser(User user);

    void deleteUser(Long id);


}
