package com.example.task_project_tracker.controller;

import com.example.task_project_tracker.dto.task.TaskRequest;
import com.example.task_project_tracker.dto.task.TaskResponse;
import com.example.task_project_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    final TaskService taskService;

    // POST /api/tasks - Δημιουργία νέου Task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse taskResponse = taskService.createTask(request);
        return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    // GET /api/tasks/project/{projectId} - Λήψη όλων των Tasks ενός Project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    // GET /api/tasks/{taskId} - Λήψη συγκεκριμένου Task με βάση το ID
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTasksById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // GET /api/tasks/assignee/{assigneeId} - Λήψη όλων των Tasks που έχουν ανατεθεί σε συγκεκριμένο χρήστη
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignee(@PathVariable Long assigneeId) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId));
    }

    // DELETE /api/tasks/{taskId} - Διαγραφή Task με βάση το ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/tasks/{taskId} - Ενημέρωση Task με βάση το ID
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        TaskResponse taskResponse = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(taskResponse);
    }
}
