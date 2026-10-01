package com.example.task_project_tracker.service;

import com.example.task_project_tracker.dto.user.UserRequest;
import com.example.task_project_tracker.dto.user.LoginRequest;
import com.example.task_project_tracker.dto.user.UserResponse;
import com.example.task_project_tracker.exception.ResourceNotFoundException;
import com.example.task_project_tracker.mapper.UserMapper;
import com.example.task_project_tracker.model.User;
import com.example.task_project_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // 1. Επιστρέφει όλους τους χρήστες (για να εμφανιστούν στο Dropdown ανάθεσης)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // 2. Δημιουργία νέου χρήστη
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists" + request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // 3. Login χρήστη
    public UserResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.email()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return userMapper.toResponse(user);
    }

    // 4. Επιστρέφει έναν χρήστη με βάση το ID
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

}
