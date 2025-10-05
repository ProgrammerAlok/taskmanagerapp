package com.example.taskmanagerapp.dto;

import com.example.taskmanagerapp.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull public Long requesterUserId; // who is requesting
    @NotNull public TaskStatus status;
}
