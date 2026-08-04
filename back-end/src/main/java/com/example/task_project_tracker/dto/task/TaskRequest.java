package com.example.task_project_tracker.dto.task;

import com.example.task_project_tracker.model.TaskPriority;
import com.example.task_project_tracker.model.TaskStatus;
import java.time.LocalDate;

public record TaskRequest(
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        Long projectId,  // Στέλνουμε μόνο το ID του project
        Long assigneeId  // Στέλνουμε μόνο το ID του χρήστη
) {}
