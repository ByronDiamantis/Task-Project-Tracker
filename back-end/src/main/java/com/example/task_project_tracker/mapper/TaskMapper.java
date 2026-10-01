package com.example.task_project_tracker.mapper;

import com.example.task_project_tracker.dto.task.TaskRequest;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.model.Project;
import com.example.task_project_tracker.model.Task;
import com.example.task_project_tracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserMapper userMapper;

    public TaskResponse toResponse(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getProject() != null ? task.getProject().getId() : null,
                userMapper.toResponse(task.getAssignee())
        );
    }

    // Διόρθωση: Αφαιρέθηκε το περιττό άγκιστρο και προστέθηκαν τα entities Project & Assignee
    public Task toEntity(TaskRequest request, Project project, User assignee) {
        if (request == null) {
            return null;
        }
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .project(project)
                .assignee(assignee)
                .build();
    }
}