package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.UserController;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
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

        userResponseEntityModel.add(linkTo(methodOn(UserController.class).getUser(userResponse.getId())).withSelfRel());

        return userResponseEntityModel;

    }

}
