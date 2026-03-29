package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance;

import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.TaskEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper.TaskJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReadTaskAdapter implements ReadTaskPort {

    private final TaskJpaRepository taskJpaRepository;

    private final TaskJpaMapper taskJpaMapper;

    private final InboundPaginationMapper paginationMapper;

    @Override
    public Optional<Task> findById(Long id) {

        return taskJpaRepository.findById(id)
                .map(taskEntity -> taskJpaMapper.toDomainModel(taskEntity, id));
    }

    @Override
    public Pagination<Task> findAll(Long id, Pagination<Task> pagination) {

        Pageable pageable = paginationMapper.fromPaginationToPageable(pagination);

        Page<TaskEntity> page = taskJpaRepository.findAll(pageable);

        pagination.setContent(taskJpaMapper.toDomainModels(page.getContent(), id));

        pagination.setTotalElements(page.getTotalElements());

        pagination.setTotalPages(page.getTotalPages());

        return pagination;

    }
}
