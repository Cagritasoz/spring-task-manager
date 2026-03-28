package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;

import java.util.Optional;

public interface ReadUserPort {

    Optional<User> findById(Long id);

    Pagination<User> findAll(Pagination<User> pagination);

    Optional<User> findByEmail(String email);

    boolean existsById(Long id);

    boolean existsByEmail(String email);



}
