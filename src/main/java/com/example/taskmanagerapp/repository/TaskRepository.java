package com.example.taskmanagerapp.repository;

import com.example.taskmanagerapp.entity.Task;
import com.example.taskmanagerapp.entity.TaskStatus;
import com.example.taskmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByAssignedUserAndStatus(User assignedUser, TaskStatus status);
    List<Task> findByCreatedByAndStatus(User createdBy, TaskStatus status);
    List<Task> findByCreatedByAndDueDateBefore(User createdBy, OffsetDateTime dueDate);
    List<Task> findByAssignedUserAndStatus(User assignedUser, TaskStatus status);
    List<Task> findByAssignedUserAndDueDateBefore(User assignedUser, OffsetDateTime dueDate);
}
