package com.example.task_project_tracker.mapper;

import com.example.task_project_tracker.dto.project.ProjectRequest;
import com.example.task_project_tracker.dto.project.ProjectResponse;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.model.Project;
import com.example.task_project_tracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final TaskMapper taskMapper;

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }

        List<TaskResponse> taskResponses = project.getTasks() != null
                ? project.getTasks().stream().map(taskMapper::toResponse).toList()
                : List.of();

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getOwner() != null ? project.getOwner().getId() : null,
                project.getOwner() != null ? project.getOwner().getUsername() : null,
                taskResponses
        );
    }

    // Διόρθωση: Δέχεται ProjectRequest και τον User owner για σωστή συσχέτιση
    public Project toEntity(ProjectRequest request, User owner) {
        if (request == null) {
            return null;
        }
        Project project = new Project();
        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setOwner(owner);
        return project;
    }
}