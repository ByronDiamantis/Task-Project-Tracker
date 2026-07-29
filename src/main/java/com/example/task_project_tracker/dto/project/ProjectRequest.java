package com.example.task_project_tracker.dto.project;

import java.time.LocalDateTime;

public record ProjectRequest (
        String name,
        String description,
        Long ownerId
) {}
