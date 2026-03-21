package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UserResponse userResponse;

    private JwtResponse jwtResponse;

}
