package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto;

public record RegisterUserRequest(String username,
                                  String email,
                                  String password) {

}
