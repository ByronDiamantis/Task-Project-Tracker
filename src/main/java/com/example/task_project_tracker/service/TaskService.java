package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.task.TaskRequest;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.dto.user.UserResponse;
import com.example.task_project_tracker.model.Project;
import com.example.task_project_tracker.model.Task;
import com.example.task_project_tracker.model.User;
import com.example.task_project_tracker.repository.ProjectRepository;
import com.example.task_project_tracker.repository.TaskRepository;
import com.example.task_project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    // 1. Δημιουργία Νέου Task
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .project(project)
                .assignee(assignee)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    // 2. Λήψη όλων των Tasks για ένα Project
    public List<TaskResponse> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    // 3. Helper μέθοδος μετατροπής Task Entity -> TaskResponse DTO
    private TaskResponse mapToTaskResponse (Task task) {
        UserResponse assigneeResponse = null;
        if (task.getAssignee() != null) {
            assigneeResponse = new UserResponse(
                    task.getAssignee().getId(),
                    task.getAssignee().getUsername(),
                    task.getAssignee().getEmail(),
                    task.getAssignee().getCreatedAt()
            );
        }
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getProject().getId(),
                assigneeResponse
        );
    }
}
