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

    private String token;

    private EntityModel<UserResponse> user;

    @JsonIgnore
    public UserResponse getContent() {

        return user.getContent();

    }

}
