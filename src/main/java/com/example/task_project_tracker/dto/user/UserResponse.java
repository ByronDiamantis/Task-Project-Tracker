package com.example.task_project_tracker.dto.user;

import java.time.LocalDateTime;

public record UserResponse (
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {}
