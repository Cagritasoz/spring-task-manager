package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;




import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import org.springframework.stereotype.Component;



@Component
public class UserMapper {

    public User toDomainModel(RegisterRequest registerRequest) {

        return new User(null,
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                null);

    }

    public UserResponse toDtoModel(User user) {

        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }





}
