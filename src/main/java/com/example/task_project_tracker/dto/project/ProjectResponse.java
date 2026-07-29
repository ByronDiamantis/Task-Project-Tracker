package com.example.task_project_tracker.dto.project;

import com.example.task_project_tracker.dto.user.UserResponse;
import java.time.LocalDateTime;

public record ProjectResponse (
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    UserResponse owner
) {}
