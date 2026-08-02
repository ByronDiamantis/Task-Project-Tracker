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
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    // 1. Δημιουργία Νέου Task
    @Transactional
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
                .map(TaskService::mapToTaskResponse)
                .toList();
    }

    // 3. Διαγραφή Task
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    // 4. Επεξεργασία / Ενημέρωση Task
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        if (request.assigneeId() != null) {
            User newAssignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(newAssignee);
        } else {
            task.setAssignee(null);
        }

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    // 5. Λήψη συγκεκριμένου Task με βάση το ID
    public TaskResponse getTaskById (Long taskId) {
        Task task  = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToTaskResponse(task);
    }

    //  Helper μέθοδος μετατροπής Task Entity -> TaskResponse DTO
    protected static TaskResponse mapToTaskResponse(Task task) {
        UserResponse assigneeResponse = null;
        if (task.getAssignee() != null) {
            assigneeResponse = new UserResponse(
                    task.getAssignee().getId(),
                    task.getAssignee().getUsername(),
                    task.getAssignee().getEmail()
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
