package com.example.task_project_tracker.dto.task;

import com.example.task_project_tracker.dto.user.UserResponse;
import com.example.task_project_tracker.model.TaskPriority;
import com.example.task_project_tracker.model.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse (
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        Long projectId,
        UserResponse assignee
) {}


