package com.example.taskmanagerapp.dto;

import com.example.taskmanagerapp.entity.TaskStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class CreateTaskRequest {
    @NotBlank public String title;
    @NotBlank public String description;
    @NotNull public TaskStatus status;
    @NotNull @Future public OffsetDateTime dueDate;
    @NotNull public Long assignedUserId;
    @NotNull public Long createdById;
}
