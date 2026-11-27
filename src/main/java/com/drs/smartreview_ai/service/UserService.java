package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.UserRegistrationRequest;
import com.drs.smartreview_ai.dto.UserResponse;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.enums.UserRole;
import com.drs.smartreview_ai.exception.ResourceAlreadyExistsException;
import com.drs.smartreview_ai.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(UserRegistrationRequest request) {

        // Check duplicate email
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Email already registered"
            );
        }

        // Create new user
        User user = new User();

        user.setName(request.getName());
        user.setEmail(email);

        // BCrypt password hashing
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // Assign default role
        user.setRole(UserRole.BUSINESS_OWNER);

        // Activate new account
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

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