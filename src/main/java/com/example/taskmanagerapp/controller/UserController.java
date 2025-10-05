package com.example.taskmanagerapp.controller;

import com.example.taskmanagerapp.dto.CreateUserRequest;
import com.example.taskmanagerapp.entity.User;
import com.example.taskmanagerapp.service.UserService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){ this.userService = userService; }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest req) {
        User u = userService.createUser(req.name, req.email);
        return ResponseEntity.status(201).body(u);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
}
