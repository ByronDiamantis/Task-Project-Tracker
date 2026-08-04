package com.example.task_project_tracker.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectRequest (
        @NotBlank(message = "Project title is required")
        String name,
        String description,
        @NotNull(message = "Owner ID is required")
        Long ownerId
) {}
