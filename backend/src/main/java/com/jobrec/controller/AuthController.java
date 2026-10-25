package com.jobrec.controller;

import com.jobrec.dto.AuthRequest;
import com.jobrec.dto.AuthResponse;
import com.jobrec.dto.RegisterRequest;
import com.jobrec.dto.UserProfileDTO;
import com.jobrec.entity.User;
import com.jobrec.service.AuthService;
import com.jobrec.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserProfileService userProfileService;

    public AuthController(AuthService authService, UserProfileService userProfileService) {
        this.authService = authService;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(userProfileService.getProfile(user));
    }
}
