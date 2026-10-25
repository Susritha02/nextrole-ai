package com.jobrec.service;

import com.jobrec.dto.RecommendationResultDTO;
import com.jobrec.entity.Job;
import com.jobrec.entity.JobSkill;
import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.UserSkillRepository;
import com.jobrec.util.TfIdfVectorEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationEngine {

    private final JobRepository jobRepository;
    private final UserSkillRepository userSkillRepository;
    private final JobService jobService;

    public RecommendationEngine(JobRepository jobRepository,
                                UserSkillRepository userSkillRepository,
                                JobService jobService) {
        this.jobRepository = jobRepository;
        this.userSkillRepository = userSkillRepository;
        this.jobService = jobService;
    }

    public List<RecommendationResultDTO> recommendJobsForUser(User user, int limit) {
        List<UserSkill> userSkillsList = userSkillRepository.findByUser(user);
        Set<String> userSkillNames = userSkillsList.stream()
                .map(us -> us.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        List<Job> allJobs = jobRepository.findAll();
        List<RecommendationResultDTO> recommendations = new ArrayList<>();

        for (Job job : allJobs) {
            RecommendationResultDTO rec = evaluateJobMatch(job, user, userSkillNames);
            recommendations.add(rec);
        }

        // Sort descending by match percentage
        recommendations.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));

        if (limit > 0 && recommendations.size() > limit) {
            return recommendations.subList(0, limit);
        }
        return recommendations;
    }

    public RecommendationResultDTO evaluateJobMatch(Job job, User user, Set<String> userSkillNames) {
        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        List<String> allRequired = new ArrayList<>();

        double totalRequiredWeight = 0.0;
        double matchedWeight = 0.0;

        List<JobSkill> jobSkills = job.getJobSkills() != null ? job.getJobSkills() : Collections.emptyList();

        for (JobSkill js : jobSkills) {
            String skillName = js.getSkill().getName();
            double weight = js.getWeight() != null ? js.getWeight() : 1.0;
            if (Boolean.TRUE.equals(js.getIsRequired())) {
                weight *= 1.2; // Higher importance for mandatory skills
            }
            totalRequiredWeight += weight;

            allRequired.add(skillName);

            if (userSkillNames.contains(skillName.toLowerCase())) {
                matchingSkills.add(skillName);
                matchedWeight += weight;
            } else {
                missingSkills.add(skillName);
            }
        }

        // 1. Skill Score (0 to 100)
        double skillScore = 0.0;
        if (totalRequiredWeight > 0) {
            skillScore = (matchedWeight / totalRequiredWeight) * 100.0;
        } else {
            skillScore = 70.0; // Default if no skills explicitly listed
        }

        // Incorporate TF-IDF text similarity between user bio/headline and job description
        String userProfileText = String.join(" ", userSkillNames) + " " +
                (user.getHeadline() != null ? user.getHeadline() : "") + " " +
                (user.getBio() != null ? user.getBio() : "");
        String jobText = job.getTitle() + " " + job.getDescription() + " " +
                (job.getResponsibilities() != null ? job.getResponsibilities() : "");
        double tfIdfSimilarity = TfIdfVectorEngine.computeCosineSimilarity(userProfileText, jobText);
        double tfIdfScore = Math.min(100.0, tfIdfSimilarity * 100.0 * 2.5); // Normalized boost

        // Blended skill score (80% discrete skill overlap + 20% NLP text similarity)
        double blendedSkillScore = Math.min(100.0, (skillScore * 0.8) + (tfIdfScore * 0.2));

        // 2. Experience Compatibility (0 to 100)
        double userExp = user.getExperienceYears() != null ? user.getExperienceYears() : 1.0;
        double minReqExp = job.getMinExperienceYears() != null ? job.getMinExperienceYears() : 0.0;
        double maxReqExp = job.getMaxExperienceYears() != null ? job.getMaxExperienceYears() : 5.0;

        double experienceScore;
        String experienceCompatibility;

        if (userExp >= minReqExp && userExp <= maxReqExp) {
            experienceScore = 100.0;
            experienceCompatibility = String.format("High Match: Your %.1fy exp fits target range (%.1f - %.1fy)",
                    userExp, minReqExp, maxReqExp);
        } else if (userExp >= minReqExp) {
            experienceScore = 90.0; // Overqualified slightly but qualified
            experienceCompatibility = String.format("Experienced: Your %.1fy exp exceeds minimum %.1fy",
                    userExp, minReqExp);
        } else {
            // Under minimum
            double ratio = minReqExp > 0 ? (userExp / minReqExp) : 1.0;
            experienceScore = Math.max(30.0, ratio * 85.0);
            experienceCompatibility = String.format("Growth Role: %.1fy exp vs %.1fy recommended",
                    userExp, minReqExp);
        }

        // 3. Work Mode & Location Compatibility (0 to 100)
        double workModeScore = 80.0;
        String locationCompatibility;

        String userMode = user.getPreferredWorkMode() != null ? user.getPreferredWorkMode().toUpperCase() : "REMOTE";
        String jobMode = job.getWorkMode() != null ? job.getWorkMode().toUpperCase() : "REMOTE";

        boolean modeMatches = "ANY".equals(userMode) || "REMOTE".equals(jobMode) || userMode.equals(jobMode);
        boolean locationMatches = user.getLocation() != null && job.getLocation() != null &&
                (job.getLocation().toLowerCase().contains(user.getLocation().toLowerCase()) ||
                 user.getLocation().toLowerCase().contains(job.getLocation().toLowerCase()));

        if (modeMatches && (locationMatches || "REMOTE".equals(jobMode))) {
            workModeScore = 100.0;
            locationCompatibility = "Optimal (Aligned work mode & location)";
        } else if (modeMatches) {
            workModeScore = 90.0;
            locationCompatibility = "Good (Work mode aligned)";
        } else {
            workModeScore = 65.0;
            locationCompatibility = "Requires Relocation / Onsite Presence";
        }

        // Combined Overall Match Percentage:
        // Skills: 60%, Experience: 25%, Work Mode: 15%
        double totalMatchScore = (blendedSkillScore * 0.60) + (experienceScore * 0.25) + (workModeScore * 0.15);
        totalMatchScore = Math.round(totalMatchScore * 10.0) / 10.0; // Round to 1 decimal place

        // Build Explanation
        StringBuilder expBuilder = new StringBuilder();
        if (matchingSkills.size() >= 3) {
            expBuilder.append("Strong skill alignment on ")
                    .append(String.join(", ", matchingSkills.subList(0, Math.min(3, matchingSkills.size()))));
        } else if (!matchingSkills.isEmpty()) {
            expBuilder.append("Direct match on ")
                    .append(String.join(", ", matchingSkills));
        } else {
            expBuilder.append("Foundational skills align with the core requirements");
        }

        if (!missingSkills.isEmpty()) {
            expBuilder.append(". You can boost your candidacy by acquiring ")
                    .append(missingSkills.get(0));
            if (missingSkills.size() > 1) {
                expBuilder.append(" and ").append(missingSkills.get(1));
            }
        }
        expBuilder.append(". ").append(experienceCompatibility).append(".");

        RecommendationResultDTO dto = new RecommendationResultDTO();
        dto.setJob(jobService.toDTO(job, user));
        dto.setMatchPercentage(totalMatchScore);
        dto.setMatchingSkills(matchingSkills);
        dto.setMissingSkills(missingSkills);
        dto.setRequiredSkills(allRequired);
        dto.setExperienceCompatibility(experienceCompatibility);
        dto.setLocationCompatibility(locationCompatibility);
        dto.setSkillScore(Math.round(blendedSkillScore * 10.0) / 10.0);
        dto.setExperienceScore(Math.round(experienceScore * 10.0) / 10.0);
        dto.setWorkModeScore(Math.round(workModeScore * 10.0) / 10.0);
        dto.setExplanation(expBuilder.toString());

        return dto;
    }
}
