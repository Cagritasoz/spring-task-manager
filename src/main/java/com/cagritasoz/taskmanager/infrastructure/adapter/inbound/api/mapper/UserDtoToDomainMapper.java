package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;




import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import org.springframework.stereotype.Component;



@Component
public class UserDtoToDomainMapper {

    public User toDomainModel(RegisterRequest registerRequest) {

        return new User(null,
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                null);

    }

    public User toDomainModel(CreateUserRequest createUserRequest) {

        return new User(null,
                createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getRole()); //admin can set the role!

    }

}
