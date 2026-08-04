package com.example.task_project_tracker.controller;

import com.example.task_project_tracker.dto.user.LoginRequest;
import com.example.task_project_tracker.dto.user.UserRequest;
import com.example.task_project_tracker.dto.user.UserResponse;
import com.example.task_project_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    // POST /api/users/register - Εγγραφή νέου χρήστη
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    // POST /api/users/login - Σύνδεση χρήστη
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody LoginRequest request) {
        UserResponse response = userService.LoginUser(request);
        return ResponseEntity.ok(response);
    }

    // GET /api/users - Λήψη όλων των χρηστών (για dropdowns)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
