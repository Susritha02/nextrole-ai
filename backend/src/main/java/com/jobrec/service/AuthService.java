package com.jobrec.service;

import com.jobrec.dto.AuthRequest;
import com.jobrec.dto.AuthResponse;
import com.jobrec.dto.RegisterRequest;
import com.jobrec.entity.User;
import com.jobrec.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email is already registered: " + request.getEmail());
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setHeadline(request.getHeadline() != null && !request.getHeadline().isBlank()
                ? request.getHeadline().trim() : "Aspiring Software Professional");
        user.setLocation(request.getLocation() != null && !request.getLocation().isBlank()
                ? request.getLocation().trim() : "Remote / Open");
        user.setExperienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 1.0);
        user.setPreferredWorkMode(request.getPreferredWorkMode() != null ? request.getPreferredWorkMode() : "REMOTE");
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User.withUsername(savedUser.getEmail())
                        .password(savedUser.getPasswordHash())
                        .authorities(savedUser.getRole())
                        .build();

        String token = jwtService.generateToken(userDetails, savedUser.getId(), savedUser.getFullName());
        return new AuthResponse(token, savedUser.getId(), savedUser.getFullName(), savedUser.getEmail(),
                savedUser.getHeadline(), savedUser.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .authorities(user.getRole())
                        .build();

        String token = jwtService.generateToken(userDetails, user.getId(), user.getFullName());
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(),
                user.getHeadline(), user.getRole());
    }

    public User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
    }
}
