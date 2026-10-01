package com.example.task_project_tracker.dto.task;

import com.example.task_project_tracker.model.TaskPriority;
import com.example.task_project_tracker.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        @NotNull(message = "Project ID is required")
        Long projectId,  // Στέλνουμε μόνο το ID του project
        Long assigneeId  // Στέλνουμε μόνο το ID του χρήστη
) {}
