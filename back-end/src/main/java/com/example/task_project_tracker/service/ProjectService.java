package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.project.ProjectRequest;
import com.example.task_project_tracker.dto.project.ProjectResponse;
import com.example.task_project_tracker.exception.ResourceNotFoundException;
import com.example.task_project_tracker.mapper.ProjectMapper;
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
    private final ProjectMapper projectMapper;

    // 1. Δημιουργία Νέου Project
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.ownerId()));

        Project project = projectMapper.toEntity(request, owner);
        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }

    // 2. Ανάκτηση όλων των Projects που ανήκουν σε έναν χρήστη
    public List<ProjectResponse> getProjectByOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    // 3. Αναζήτηση συγκεκριμένου Project με βάση το ID
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        return projectMapper.toResponse(project);
    }

    // 4. Διαγραφή Project με βάση το ID
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        projectRepository.delete(project);
    }

}
