package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;

import java.util.Optional;

public interface ReadTaskPort {

    Optional<Task> findById(Long id);

    Pagination<Task> findAll(Long id, Pagination<Task> pagination);

    boolean existsById(Long id);

}
