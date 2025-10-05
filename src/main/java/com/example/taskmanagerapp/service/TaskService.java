package com.example.taskmanagerapp.service;

import com.example.taskmanagerapp.entity.Task;
import com.example.taskmanagerapp.entity.TaskStatus;
import com.example.taskmanagerapp.entity.User;
import com.example.taskmanagerapp.exception.BadRequestException;
import com.example.taskmanagerapp.exception.ForbiddenException;
import com.example.taskmanagerapp.exception.ResourceNotFoundException;
import com.example.taskmanagerapp.repository.TaskRepository;
import com.example.taskmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private static final int MAX_PENDING = 10;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public Task createTask(String title, String description, TaskStatus status,
                           OffsetDateTime dueDate, Long assignedUserId, Long createdById) {

        User creator = userRepo.findById(createdById).orElseThrow(() -> new ResourceNotFoundException("creator_not_found"));
        User assigned = null;
        if (assignedUserId != null) {
            assigned = userRepo.findById(assignedUserId).orElseThrow(() -> new ResourceNotFoundException("assigned_user_not_found"));
        }

        // Business rule: assigned user's pending tasks cannot exceed MAX_PENDING
        if (assigned != null && (status == TaskStatus.PENDING || status == null)) {
            long pendingCount = taskRepo.countByAssignedUserAndStatus(assigned, TaskStatus.PENDING);
            if (pendingCount >= MAX_PENDING) {
                throw new BadRequestException("assigned_user_has_too_many_pending_tasks");
            }
        }

        Task t = new Task();
        t.setTitle(title);
        t.setDescription(description);
        t.setStatus(status != null ? status : TaskStatus.PENDING);
        t.setDueDate(dueDate);
        t.setAssignedUser(assigned);
        t.setCreatedBy(creator);
        t.setUpdatedBy(creator);

        return taskRepo.save(t);
    }

    public Task updateStatus(Long taskId, Long requesterUserId, TaskStatus newStatus) {
        Task t = taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("task_not_found"));
        User requester = userRepo.findById(requesterUserId).orElseThrow(() -> new ResourceNotFoundException("user_not_found"));

        // Only assigned user or creator can change status
        boolean isAssigned = t.getAssignedUser() != null && t.getAssignedUser().getId().equals(requesterUserId);
        boolean isCreator = t.getCreatedBy() != null && t.getCreatedBy().getId().equals(requesterUserId);
        if (!isAssigned && !isCreator) {
            throw new ForbiddenException("not_authorized_to_change_status");
        }

        // If changing to PENDING, ensure user doesn't exceed MAX_PENDING
        if (newStatus == TaskStatus.PENDING && t.getAssignedUser() != null) {
            long pendingCount = taskRepo.countByAssignedUserAndStatus(t.getAssignedUser(), TaskStatus.PENDING);
            // if current task is already pending, don't double-count
            if (t.getStatus() != TaskStatus.PENDING && pendingCount >= MAX_PENDING) {
                throw new BadRequestException("assigned_user_has_too_many_pending_tasks");
            }
        }

        t.setStatus(newStatus);
        t.setUpdatedBy(requester);
        return taskRepo.save(t);
    }

    public List<Task> listCreatedTasks(Long userId, TaskStatus status, OffsetDateTime dueBefore) {
        User u = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user_not_found"));
        if (status != null && dueBefore != null) {
            return taskRepo.findByCreatedByAndStatus(u, status).stream()
                    .filter(t -> t.getDueDate()!=null && t.getDueDate().isBefore(dueBefore))
                    .toList();
        } else if (status != null) {
            return taskRepo.findByCreatedByAndStatus(u, status);
        } else if (dueBefore != null) {
            return taskRepo.findByCreatedByAndDueDateBefore(u, dueBefore);
        }
        // fallback: return all created by user
        return taskRepo.findAll().stream().filter(t-> t.getCreatedBy().getId().equals(userId)).toList();
    }

    public List<Task> listAssignedTasks(Long userId, TaskStatus status, OffsetDateTime dueBefore) {
        User u = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user_not_found"));
        if (status != null && dueBefore != null) {
            return taskRepo.findByAssignedUserAndStatus(u, status).stream()
                    .filter(t -> t.getDueDate()!=null && t.getDueDate().isBefore(dueBefore))
                    .toList();
        } else if (status != null) {
            return taskRepo.findByAssignedUserAndStatus(u, status);
        } else if (dueBefore != null) {
            return taskRepo.findByAssignedUserAndDueDateBefore(u, dueBefore);
        }
        return taskRepo.findAll().stream().filter(t -> t.getAssignedUser()!=null && t.getAssignedUser().getId().equals(userId)).toList();
    }

    public void deleteTask(Long taskId, Long requesterUserId) {
        Task t = taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("task_not_found"));
        if (!t.getCreatedBy().getId().equals(requesterUserId)) {
            throw new ForbiddenException("only_creator_can_delete_task");
        }
        taskRepo.delete(t);
    }
}
