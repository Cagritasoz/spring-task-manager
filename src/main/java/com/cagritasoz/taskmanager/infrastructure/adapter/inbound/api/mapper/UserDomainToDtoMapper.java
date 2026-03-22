package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.JwtUser;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDomainToDtoMapper {

    public UserResponse toDtoModel(JwtUser jwtUser) {

        User user = jwtUser.getUser();

        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());

    }

    public UserResponse toDtoModel(User user) {

        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());

    }

    public List<UserResponse> toDtoModels(List<User> userList) {

        return userList.stream()
                .map(this::toDtoModel)
                .toList();

    }
}
