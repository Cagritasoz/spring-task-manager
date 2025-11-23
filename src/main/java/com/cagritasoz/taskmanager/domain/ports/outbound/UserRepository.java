package com.cagritasoz.taskmanager.domain.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User saveUser(User user);

    boolean deleteUser(Long id);

}
