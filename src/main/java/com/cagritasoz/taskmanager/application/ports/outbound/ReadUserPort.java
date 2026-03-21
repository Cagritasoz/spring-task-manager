package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.User;

import java.util.Optional;

public interface ReadUserPort {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);



}
