package com.example.task_project_tracker.controller;

import com.example.task_project_tracker.dto.project.ProjectRequest;
import com.example.task_project_tracker.dto.project.ProjectResponse;
import com.example.task_project_tracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    final ProjectService projectService;

    // POST /api/projects - Δημιουργία νέου Project
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse projectResponse = projectService.createProject(request);
        return new ResponseEntity<>(projectResponse, HttpStatus.CREATED);
    }

    // GET /api/projects/owner/{ownerId} - Λήψη όλων των Projects ενός χρήστη
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(projectService.getProjectByOwnerId(ownerId));
    }

    // GET /api/projects/{id} - Λήψη συγκεκριμένου Project με τα Tasks του
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // DELETE /api/projects/{id} - Διαγραφή Project με βάση το ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
