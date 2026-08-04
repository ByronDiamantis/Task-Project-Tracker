package com.example.task_project_tracker.dto.project;

import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.dto.user.UserResponse;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse (
        Long id,
        String title,
        String description,
        Long ownerId,
        String ownerName,
        List<TaskResponse> tasks
) {}
