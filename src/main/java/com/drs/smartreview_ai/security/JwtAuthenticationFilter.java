package com.drs.smartreview_ai.security;

import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // No token: continue. Protected routes will be rejected by Spring Security.
        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            sendUnauthorized(response);
            return;
        }

        String token = authorizationHeader.substring(7);

        User user;

        try {
            String email = jwtService.extractEmail(token);

            user = userRepository.findByEmail(email).orElse(null);

        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response);
            return;
        }

        if (user == null || !Boolean.TRUE.equals(user.getIsActive())) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                )
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // Keep this outside the JWT validation try-catch.
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"message\":\"Invalid or expired authentication token\"}"
        );
    }
}