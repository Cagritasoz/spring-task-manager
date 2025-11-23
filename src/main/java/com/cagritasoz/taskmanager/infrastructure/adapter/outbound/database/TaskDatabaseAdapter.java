package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database;


import com.cagritasoz.taskmanager.domain.ports.outbound.TaskRepository;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class TaskDatabaseAdapter implements TaskRepository {

    @PersistenceContext
    private EntityManager em;

    private final TaskMapper taskMapper;

    public TaskDatabaseAdapter(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }





}
