package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database.mapper;

import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database.entity.UserEntity;
import org.springframework.stereotype.Component;


@Component
//@Bean → used inside @Configuration class on only methods. Spring calls the method
//and registers the returned object as a bean. Use for third-party beans such as
//PasswordEncoder, ObjectMapper, JWT utilities, WebClient, etc.
public class UserMapper {

    public User toDomain(UserEntity userEntity) { //If converting to domain the object must have already existed in the database anyway. It already has an ID.
        User user = new User(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());
        user.setId(userEntity.getId());
        return user;
    }

    public UserEntity toEntity(User user) {
        if(user.getId() != null) { //User exists in the database.
            UserEntity userEntity = new UserEntity(user.getUsername(), user.getEmail(), user.getPassword());
            userEntity.setId(user.getId());
            return userEntity;
        }
        return new UserEntity(user.getUsername(), user.getEmail(), user.getPassword()); //ID field is null meaning that this is a new row.
    }

}


