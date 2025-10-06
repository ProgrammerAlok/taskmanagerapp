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
    /*
        SELECT COUNT(*) 
        FROM tasks 
        WHERE assigned_user_id = ? AND status = ?;

    */
    long countByAssignedUserAndStatus(User assignedUser, TaskStatus status);
    
    /*
        SELECT id, title, description, status, due_date, assigned_user_id, created_by_id, updated_by_id, created_at, updated_at 
        FROM tasks 
        WHERE created_by_id = ? AND status = ?;

    */
    List<Task> findByCreatedByAndStatus(User createdBy, TaskStatus status);

    /*
        SELECT id, title, description, status, due_date, assigned_user_id, created_by_id, updated_by_id, created_at, updated_at 
        FROM tasks 
        WHERE created_by_id = ? AND due_date < ?;

    */
    List<Task> findByCreatedByAndDueDateBefore(User createdBy, OffsetDateTime dueDate);

    /*
        SELECT id, title, description, status, due_date, assigned_user_id, created_by_id, updated_by_id, created_at, updated_at 
        FROM tasks 
        WHERE assigned_user_id = ? AND status = ?;

    */
    List<Task> findByAssignedUserAndStatus(User assignedUser, TaskStatus status);

    /*
        SELECT id, title, description, status, due_date, assigned_user_id, created_by_id, updated_by_id, created_at, updated_at 
        FROM tasks 
        WHERE assigned_user_id = ? AND due_date < ?;

    */
    List<Task> findByAssignedUserAndDueDateBefore(User assignedUser, OffsetDateTime dueDate);
}
