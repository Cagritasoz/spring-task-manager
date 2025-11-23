package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto;

public record RegisterUserResponse(Long userId,
                                   String username,
                                   String email) {

}
