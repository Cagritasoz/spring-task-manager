package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserEndpointAdapter findUserEndpointAdapter;

    private final ChangeUserEndpointAdapter changeUserEndpointAdapter;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) { //Admin only!

        EntityModel<UserResponse> userResponseEntityModel = changeUserEndpointAdapter.createUser(createUserRequest);

        Link selfLink = userResponseEntityModel.getRequiredLink("self");

        URI location = selfLink.toUri();

        return ResponseEntity.created(location).body(userResponseEntityModel);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable Long id) {

        EntityModel<UserResponse> userResponseEntityModel = findUserEndpointAdapter.getUser(id);

        return ResponseEntity.ok(userResponseEntityModel);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserResponse>>> getUsers(
            @PageableDefault(size = 3) Pageable pageable) { //size 3 for easy testing, page is 0 by default.
                                                            // We can also specify default sort and default asc or desc.

        PagedModel<EntityModel<UserResponse>> pagedModel = findUserEndpointAdapter.getUsers(pageable);

        return ResponseEntity.ok(pagedModel);
    }




}
