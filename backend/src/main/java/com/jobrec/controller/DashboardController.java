package com.jobrec.controller;

import com.jobrec.dto.DashboardOverviewDTO;
import com.jobrec.entity.JobApplication;
import com.jobrec.entity.User;
import com.jobrec.repository.JobApplicationRepository;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.SavedJobRepository;
import com.jobrec.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final AuthService authService;
    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobApplicationRepository applicationRepository;
    private final RecommendationEngine recommendationEngine;
    private final ApplicationTrackerService applicationTrackerService;
    private final UserProfileService userProfileService;

    public DashboardController(AuthService authService,
                               JobRepository jobRepository,
                               SavedJobRepository savedJobRepository,
                               JobApplicationRepository applicationRepository,
                               RecommendationEngine recommendationEngine,
                               ApplicationTrackerService applicationTrackerService,
                               UserProfileService userProfileService) {
        this.authService = authService;
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
        this.applicationRepository = applicationRepository;
        this.recommendationEngine = recommendationEngine;
        this.applicationTrackerService = applicationTrackerService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDTO> getOverview() {
        User user = authService.getCurrentAuthenticatedUser();

        DashboardOverviewDTO dto = new DashboardOverviewDTO();
        dto.setTotalJobsCount(jobRepository.count());
        dto.setSavedJobsCount(savedJobRepository.findByUserIdOrderBySavedAtDesc(user.getId()).size());
        dto.setTotalApplicationsCount(applicationRepository.countByUserId(user.getId()));
        dto.setInterviewCount(applicationRepository.countByUserIdAndStatus(user.getId(), JobApplication.Status.INTERVIEW));
        dto.setOfferCount(applicationRepository.countByUserIdAndStatus(user.getId(), JobApplication.Status.OFFER));
        dto.setApplicationsByStatus(applicationTrackerService.getApplicationStats(user));
        dto.setTopRecommendations(recommendationEngine.recommendJobsForUser(user, 4));

        var userApps = applicationTrackerService.getUserApplications(user);
        dto.setRecentApplications(userApps.stream().limit(5).toList());

        var profile = userProfileService.getProfile(user);
        dto.setUserSkills(profile.getSkills());
        dto.setProfileExperienceYears(profile.getExperienceYears());

        return ResponseEntity.ok(dto);
    }
}
