package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private EntityModel<UserResponse> userResponse;

    private JwtResponse jwtResponse;

    @JsonIgnore
    public UserResponse getContent() {

        return userResponse.getContent();

    }

}
