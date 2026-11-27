package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.LoginRequest;
import com.drs.smartreview_ai.dto.LoginResponse;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.repository.UserRepository;
import com.drs.smartreview_ai.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        // Find user by email
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        // Reject inactive accounts
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String storedPassword = user.getPasswordHash();
        String rawPassword = request.getPassword();

        boolean passwordMatches;

        // Check BCrypt password
        if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {

            passwordMatches = passwordEncoder.matches(
                    rawPassword,
                    storedPassword
            );

        } else {

            // Legacy plaintext password verification
            passwordMatches = storedPassword.equals(rawPassword);

            // Migrate plaintext password to BCrypt
            if (passwordMatches) {

                user.setPasswordHash(
                        passwordEncoder.encode(rawPassword)
                );

                userRepository.save(user);
            }
        }

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}