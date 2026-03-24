package com.cagritasoz.taskmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserWithToken {

    private User user;

    private String jwt;

}
