package com.example.taskmanagerapp.controller;

import com.example.taskmanagerapp.dto.CreateTaskRequest;
import com.example.taskmanagerapp.dto.UpdateStatusRequest;
import com.example.taskmanagerapp.entity.Task;
import com.example.taskmanagerapp.entity.TaskStatus;
import com.example.taskmanagerapp.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.status(200).body(taskService.getAllTasks());
    }

    // Create task
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest req) {
        Task t = taskService.createTask(req.title, req.description, req.status, req.dueDate, req.assignedUserId,
                req.createdById);
        return ResponseEntity.status(201).body(t);
    }

    // Update task status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        Task t = taskService.updateStatus(id, req.requesterUserId, req.status);
        return ResponseEntity.ok(t);
    }

    // List created tasks (filter by status and dueBefore)
    @GetMapping("/created/{userId}")
    public ResponseEntity<List<Task>> listCreatedTasks(
            @PathVariable Long userId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dueBefore) {

        return ResponseEntity.ok(taskService.listCreatedTasks(userId, status, dueBefore));
    }

    // List assigned tasks
    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<Task>> listAssignedTasks(
            @PathVariable Long userId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dueBefore) {

        return ResponseEntity.ok(taskService.listAssignedTasks(userId, status, dueBefore));
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @RequestParam Long requesterUserId) {
        taskService.deleteTask(id, requesterUserId);
        return ResponseEntity.noContent().build();
    }
}
