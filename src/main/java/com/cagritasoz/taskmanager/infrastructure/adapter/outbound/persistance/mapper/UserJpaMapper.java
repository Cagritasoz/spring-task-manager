package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper;

import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserJpaMapper {

    public UserEntity toEntityModel(User user) {

        return new UserEntity(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());

    }

    public User toDomainModel(UserEntity userEntity) {

        return new User(userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole());

    }

    public List<User> toDomainModels(List<UserEntity> userEntities) {

        return userEntities.stream()
                .map(this::toDomainModel)
                .toList();

    }

}
