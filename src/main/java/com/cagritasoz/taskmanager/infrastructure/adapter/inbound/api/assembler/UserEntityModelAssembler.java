package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.TaskController;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.UserController;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserEntityModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public @NonNull EntityModel<UserResponse> toModel(@NonNull UserResponse userResponse) {

        EntityModel<UserResponse> userResponseEntityModel = EntityModel.of(userResponse);

        Long userId = userResponse.getId();

        userResponseEntityModel.add(linkTo(methodOn(UserController.class)
                .getUser(userId))
                .withSelfRel());

        userResponseEntityModel.add(linkTo(methodOn(UserController.class)
                .updateUser(userId, null))
                .withRel("update"));

        userResponseEntityModel.add(linkTo(methodOn(UserController.class)
                .deleteUser(userId))
                .withRel("delete"));

        userResponseEntityModel.add(linkTo(methodOn(TaskController.class)
                .getTasks(userId, Pageable.unpaged()))
                .withRel("tasks"));

        userResponseEntityModel.add(linkTo(methodOn(TaskController.class)
                .createTask(userId,null))
                .withRel("create-task"));

        return userResponseEntityModel;

    }

}

/*
JSON (JavaScript Object Notation) is a generic, language-independent data format for representing structured data,
while HAL (Hypertext Application Language) is a specific convention for using JSON to build hypermedia APIs.
All HAL is JSON, but not all JSON is HAL.

In REST, a link is primarily a "pointer" to another resource.
The web follows the same rule: in HTML, an <a href="..."> tag doesn't specify a method because it is always a GET.
In HATEOAS, if a link is provided without a method, the client assumes it should perform a GET to "discover" that resource.
 */
