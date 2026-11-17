package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.UserRegistrationRequest;
import com.drs.smartreview_ai.dto.UserResponse;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.enums.UserRole;
import com.drs.smartreview_ai.exception.ResourceAlreadyExistsException;
import com.drs.smartreview_ai.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(UserRegistrationRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // Create User entity
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(UserRole.BUSINESS_OWNER);

        // Save user in database
        User savedUser = userRepository.save(user);

        // Convert entity into response DTO
        return mapToUserResponse(savedUser);
    }

    private UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}