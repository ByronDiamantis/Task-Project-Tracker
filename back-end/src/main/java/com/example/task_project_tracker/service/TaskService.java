package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.task.TaskRequest;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.exception.ResourceNotFoundException;
import com.example.task_project_tracker.mapper.TaskMapper;
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
    private final TaskMapper taskMapper;

    // 1. Δημιουργία Νέου Task
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.projectId()));

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.assigneeId()));
        }

        Task task = taskMapper.toEntity(request, project, assignee);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    // 2. Λήψη όλων των Tasks για ένα Project
    public List<TaskResponse> getTasksByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // 3. Διαγραφή Task
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }

    // 4. Επεξεργασία / Ενημέρωση Task
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.projectId()));

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.assigneeId()));
        }

        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());
        existingTask.setStatus(request.status());
        existingTask.setPriority(request.priority());
        existingTask.setDueDate(request.dueDate());
        existingTask.setProject(project);
        existingTask.setAssignee(assignee);

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toResponse(updatedTask);
    }

    // 5. Λήψη συγκεκριμένου Task με βάση το ID
    public TaskResponse getTaskById (Long taskId) {
        Task task  = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return taskMapper.toResponse(task);
    }

    // 6. Λήψη tasks με βάση τον Assignee
    public List<TaskResponse> getTasksByAssignee(Long assigneeId) {
        if (!userRepository.existsById(assigneeId)) {
            throw new ResourceNotFoundException("User not found with id: " + assigneeId);
        }
        return taskRepository.findByAssigneeId(assigneeId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

}
