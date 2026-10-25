package com.jobrec.controller;

import com.jobrec.dto.JobDTO;
import com.jobrec.dto.JobFilterRequest;
import com.jobrec.entity.User;
import com.jobrec.service.AuthService;
import com.jobrec.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final AuthService authService;

    public JobController(JobService jobService, AuthService authService) {
        this.jobService = jobService;
        this.authService = authService;
    }

    private User getOptionalCurrentUser() {
        try {
            return authService.getCurrentAuthenticatedUser();
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false) Double maxExp,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {
        JobFilterRequest filter = new JobFilterRequest();
        filter.setKeyword(keyword);
        filter.setWorkMode(workMode);
        filter.setEmploymentType(employmentType);
        filter.setMaxExp(maxExp);
        filter.setSortBy(sortBy);

        User currentUser = getOptionalCurrentUser();
        return ResponseEntity.ok(jobService.getJobs(filter, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        User currentUser = getOptionalCurrentUser();
        return ResponseEntity.ok(jobService.getJobById(id, currentUser));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<Map<String, Object>> saveJob(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        boolean saved = jobService.saveJob(id, user);
        return ResponseEntity.ok(Map.of("success", true, "saved", saved));
    }

    @DeleteMapping("/{id}/save")
    public ResponseEntity<Map<String, Object>> unsaveJob(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        boolean unsaved = jobService.unsaveJob(id, user);
        return ResponseEntity.ok(Map.of("success", true, "unsaved", unsaved));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<JobDTO>> getSavedJobs() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(jobService.getSavedJobs(user));
    }
}
