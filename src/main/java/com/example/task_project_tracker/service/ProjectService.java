package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.project.ProjectRequest;
import com.example.task_project_tracker.dto.project.ProjectResponse;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.model.Project;
import com.example.task_project_tracker.model.User;
import com.example.task_project_tracker.repository.ProjectRepository;
import com.example.task_project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // 1. Δημιουργία Νέου Project
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Project project = new Project();
        project.getCreatedAt();
        project.getTitle();
        project.getDescription();
        project.setOwner(owner);

        Project savedProject = projectRepository.save(project);
        return mapToProjectResponse(savedProject);
    }

    // 2. Ανάκτηση όλων των Projects που ανήκουν σε έναν χρήστη
    public List<ProjectResponse> getProjectByOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::mapToProjectResponse)
                .toList();
    }

    // 3. Αναζήτηση συγκεκριμένου Project με βάση το ID
    public ProjectResponse getProjectsById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return mapToProjectResponse(project);
    }

    // 4. Διαγραφή Project με βάση το ID
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
    }

    // Helper method: Μετατροπή Entity σε Response DTO
    private ProjectResponse mapToProjectResponse(Project project) {
        List<TaskResponse> taskResponses = project.getTasks() != null
                ? project.getTasks().stream().map(TaskService::mapToTaskResponse).toList()
                : List.of();

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getOwner().getUsername(),
                taskResponses
        );
    }
}
