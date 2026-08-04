package com.example.task_project_tracker.dto.exception;

import java.time.LocalDateTime;

public record ErrorDetails (
        LocalDateTime timestamp,
        String message,
        String details,
        int status
) {}
