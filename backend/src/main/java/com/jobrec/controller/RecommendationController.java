package com.jobrec.controller;

import com.jobrec.dto.RecommendationResultDTO;
import com.jobrec.entity.Job;
import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.UserSkillRepository;
import com.jobrec.service.AuthService;
import com.jobrec.service.RecommendationEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationEngine recommendationEngine;
    private final AuthService authService;
    private final JobRepository jobRepository;
    private final UserSkillRepository userSkillRepository;

    public RecommendationController(RecommendationEngine recommendationEngine,
                                    AuthService authService,
                                    JobRepository jobRepository,
                                    UserSkillRepository userSkillRepository) {
        this.recommendationEngine = recommendationEngine;
        this.authService = authService;
        this.jobRepository = jobRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResultDTO>> getRecommendedJobs(
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        User user = authService.getCurrentAuthenticatedUser();
        List<RecommendationResultDTO> recommendations = recommendationEngine.recommendJobsForUser(user, limit);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<RecommendationResultDTO> evaluateJobMatch(@PathVariable Long jobId) {
        User user = authService.getCurrentAuthenticatedUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));

        List<UserSkill> skills = userSkillRepository.findByUser(user);
        Set<String> userSkillNames = skills.stream()
                .map(s -> s.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        RecommendationResultDTO result = recommendationEngine.evaluateJobMatch(job, user, userSkillNames);
        return ResponseEntity.ok(result);
    }
}
