package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance;

import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper.UserJpaMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WriteUserAdapter implements WriteUserPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    @Override
    @Transactional
    public User saveUser(User user) {

        UserEntity userEntity = userJpaRepository.save(userJpaMapper.toEntityModel(user));

        return userJpaMapper.toDomainModel(userEntity);


    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {

        Optional<UserEntity> userEntityOptional = userJpaRepository.findById(id);

        userEntityOptional.ifPresent(userEntity -> {

            userEntity.setUsername(user.getUsername());
            userEntity.setEmail(user.getEmail());
            userEntity.setPassword(user.getPassword());
            userEntity.setRole(user.getRole());

        });

        return userEntityOptional.map(userJpaMapper::toDomainModel)
                .orElse(null); //Service ensures that a null value is never returned.
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        userJpaRepository.deleteById(id);

    }
}

/*
Hibernate keeps a snapshot of your entity when it is loaded.
At flush/commit time, it compares the current object with the snapshot.
If something changed → the entity is “dirty” → UPDATE query is generated automatically.
 */
