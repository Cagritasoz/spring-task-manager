package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserEndpointAdapter findUserEndpointAdapter;

    private final ChangeUserEndpointAdapter changeUserEndpointAdapter;


    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) { //Admin only!

        return ResponseEntity.ok().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable Long id) {

        return ResponseEntity.ok().build();


    }




}
