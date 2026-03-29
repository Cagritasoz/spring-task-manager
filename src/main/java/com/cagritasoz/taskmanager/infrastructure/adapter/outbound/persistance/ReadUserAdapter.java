package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance;

import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper.UserJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReadUserAdapter implements ReadUserPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    private final InboundPaginationMapper paginationMapper;

    @Override
    public Optional<User> findById(Long id) {

        return userJpaRepository.findById(id)
                .map(userJpaMapper::toDomainModel);

    }

    @Override
    public Pagination<User> findAll(Pagination<User> pagination) {

        Pageable pageable = paginationMapper.fromPaginationToPageable(pagination);

        Page<UserEntity> page = userJpaRepository.findAll(pageable);

        pagination.setContent(userJpaMapper.toDomainModels(page.getContent()));

        pagination.setTotalElements(page.getTotalElements());

        pagination.setTotalPages(page.getTotalPages());

        return pagination;


    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userJpaRepository.findByEmail(email)
                .map(userJpaMapper::toDomainModel);

    }

    @Override
    public boolean existsById(Long id) {

        return userJpaRepository.existsById(id);

    }

    @Override
    public boolean existsByEmail(String email) {

        return userJpaRepository.existsByEmail(email);

    }
}
