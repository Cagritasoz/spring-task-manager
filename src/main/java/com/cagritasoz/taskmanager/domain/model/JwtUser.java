package com.cagritasoz.taskmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtUser {

    private User user;

    private String jwt;

}
