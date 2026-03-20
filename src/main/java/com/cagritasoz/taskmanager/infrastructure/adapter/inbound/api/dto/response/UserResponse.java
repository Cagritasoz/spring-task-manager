package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response;

import com.cagritasoz.taskmanager.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role;

}
