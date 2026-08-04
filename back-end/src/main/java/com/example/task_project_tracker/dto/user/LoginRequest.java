package com.example.task_project_tracker.dto.user;

public record LoginRequest (
        String email,
        String password
) {}
