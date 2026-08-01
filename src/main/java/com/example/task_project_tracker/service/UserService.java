package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.user.UserRequest;
import com.example.task_project_tracker.dto.user.LoginRequest;
import com.example.task_project_tracker.dto.user.UserResponse;
import com.example.task_project_tracker.model.User;
import com.example.task_project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class UserService {

    private final UserRepository userRepository;

    // 1. Επιστρέφει όλους τους χρήστες (για να εμφανιστούν στο Dropdown ανάθεσης)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // 2. Δημιουργία νέου χρήστη
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    // 3. Login χρήστη
    public UserResponse LoginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid email or password");
        }

        return mapToUserResponse(user);
    }

    // Helper method: Μετατροπή Entity σε Response DTO
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
