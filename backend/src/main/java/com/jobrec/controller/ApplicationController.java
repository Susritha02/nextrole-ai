package com.jobrec.controller;

import com.jobrec.dto.ApplicationDTO;
import com.jobrec.dto.ApplicationStatusUpdateRequest;
import com.jobrec.entity.User;
import com.jobrec.service.ApplicationTrackerService;
import com.jobrec.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationTrackerService trackerService;
    private final AuthService authService;

    public ApplicationController(ApplicationTrackerService trackerService, AuthService authService) {
        this.trackerService = trackerService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getApplications() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(trackerService.getUserApplications(user));
    }

    @PostMapping("/job/{jobId}")
    public ResponseEntity<ApplicationDTO> applyOrTrackJob(
            @PathVariable Long jobId,
            @RequestBody(required = false) ApplicationStatusUpdateRequest request
    ) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(trackerService.applyOrTrackJob(jobId, request, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationDTO> updateApplication(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateRequest request
    ) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(trackerService.updateStatus(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteApplication(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        trackerService.deleteApplication(id, user);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getApplicationStats() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(trackerService.getApplicationStats(user));
    }
}
