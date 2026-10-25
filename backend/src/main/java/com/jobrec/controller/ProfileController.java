package com.jobrec.controller;

import com.jobrec.dto.UserProfileDTO;
import com.jobrec.entity.User;
import com.jobrec.service.AuthService;
import com.jobrec.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService profileService;
    private final AuthService authService;

    public ProfileController(UserProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(profileService.getProfile(user));
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateProfile(@RequestBody UserProfileDTO updateRequest) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(profileService.updateProfile(user, updateRequest));
    }

    @PostMapping("/skills")
    public ResponseEntity<UserProfileDTO> addSkill(@RequestBody Map<String, String> body) {
        User user = authService.getCurrentAuthenticatedUser();
        String skillName = body.get("skillName");
        String proficiencyLevel = body.getOrDefault("proficiencyLevel", "INTERMEDIATE");
        return ResponseEntity.ok(profileService.addSkill(user, skillName, proficiencyLevel));
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<UserProfileDTO> removeSkill(@PathVariable Long skillId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(profileService.removeSkill(user, skillId));
    }
}
