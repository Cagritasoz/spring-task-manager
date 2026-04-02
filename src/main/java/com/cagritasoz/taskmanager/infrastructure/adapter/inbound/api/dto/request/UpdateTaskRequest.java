package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    @NotBlank
    @Size(min = 1, max = 70)
    private String title;

    @NotBlank
    @Size(min = 1, max = 300)
    private String description;

    @Future //Date must be after the current date.
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dueDate;

}
