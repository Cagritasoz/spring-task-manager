package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.UpdateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class TaskController {

    private final FindTaskEndpointAdapter findTaskEndpointAdapter;

    private final ChangeTaskEndpointAdapter changeTaskEndpointAdapter;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{userId}/tasks")
    public ResponseEntity<EntityModel<TaskResponse>> createTask(@PathVariable Long userId,
                                                                @RequestBody @Valid CreateTaskRequest createTaskRequest) {

        EntityModel<TaskResponse> taskResponseEntityModel = changeTaskEndpointAdapter.createTask(userId, createTaskRequest);

        URI location = taskResponseEntityModel.getRequiredLink("self").toUri();

        return ResponseEntity.created(location).body(taskResponseEntityModel);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<EntityModel<TaskResponse>> getTask(@PathVariable Long userId,
                                                @PathVariable Long taskId) {

        EntityModel<TaskResponse> taskResponseEntityModel = findTaskEndpointAdapter.getTask(userId, taskId);

        return ResponseEntity.ok(taskResponseEntityModel);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<PagedModel<EntityModel<TaskResponse>>> getTasks(@PathVariable Long userId,
                                                             @PageableDefault(size = 3) Pageable pageable) {

        PagedModel<EntityModel<TaskResponse>> taskResponsePagedModel = findTaskEndpointAdapter.getTasks(userId, pageable);

        return ResponseEntity.ok(taskResponsePagedModel);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<EntityModel<TaskResponse>> updateTask(@PathVariable Long userId,
                                                                @PathVariable Long taskId,
                                                                @RequestBody @Valid UpdateTaskRequest updateTaskRequest) {

        EntityModel<TaskResponse> taskResponseEntityModel = changeTaskEndpointAdapter.updateTask(
                userId,
                taskId,
                updateTaskRequest);

        return ResponseEntity.ok(taskResponseEntityModel);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long userId,
                                           @PathVariable Long taskId) {

        changeTaskEndpointAdapter.deleteTask(userId, taskId);

        return ResponseEntity.noContent().build();

    }



}
