package com.cagritasoz.taskmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role;

}
