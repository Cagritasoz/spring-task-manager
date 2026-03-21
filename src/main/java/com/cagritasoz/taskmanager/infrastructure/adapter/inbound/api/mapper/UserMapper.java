package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;




import com.cagritasoz.taskmanager.domain.model.JwtUser;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.JwtResponse;
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

    public User toDomainModel(CreateUserRequest createUserRequest) {

        return null;

    }

    public UserResponse toDtoModel(User user) {

        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());
    }

    public AuthResponse toDtoModel(JwtUser jwtUser) {

        return new AuthResponse(toDtoModel(jwtUser.getUser()), new JwtResponse(jwtUser.getJwt()));

    }





}
