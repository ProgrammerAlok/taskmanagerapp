package com.example.taskmanagerapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
    @NotBlank public String name;
    @NotBlank @Email public String email;
}
