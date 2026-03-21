package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        UserResponse userResponse = findUserEndpointAdapter.getUser(id);

        return ResponseEntity.ok(userResponse);

    }


}
