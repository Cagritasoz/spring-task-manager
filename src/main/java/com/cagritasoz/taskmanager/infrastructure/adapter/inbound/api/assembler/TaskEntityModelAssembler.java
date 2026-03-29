package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.TaskController;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskEntityModelAssembler implements RepresentationModelAssembler<TaskResponse, EntityModel<TaskResponse>> {


    @Override
    public @NonNull EntityModel<TaskResponse> toModel(@NonNull TaskResponse taskResponse) {

        EntityModel<TaskResponse> taskResponseEntityModel = EntityModel.of(taskResponse);

        taskResponseEntityModel.add(linkTo(methodOn(TaskController.class).getTask(taskResponse.getUserId(),
                taskResponse.getTaskId())).withSelfRel());

        return taskResponseEntityModel;

    }
}
