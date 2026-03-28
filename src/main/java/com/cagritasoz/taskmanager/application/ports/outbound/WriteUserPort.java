package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.User;

public interface WriteUserPort {

    User saveUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);


}
