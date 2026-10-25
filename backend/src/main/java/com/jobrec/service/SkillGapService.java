package com.jobrec.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobrec.dto.SkillGapDTO;
import com.jobrec.entity.Job;
import com.jobrec.entity.JobSkill;
import com.jobrec.entity.LearningGoal;
import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.LearningGoalRepository;
import com.jobrec.repository.UserSkillRepository;
import com.jobrec.util.SkillDictionary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillGapService {

    private final JobRepository jobRepository;
    private final UserSkillRepository userSkillRepository;
    private final LearningGoalRepository learningGoalRepository;
    private final ObjectMapper objectMapper;

    public SkillGapService(JobRepository jobRepository,
                           UserSkillRepository userSkillRepository,
                           LearningGoalRepository learningGoalRepository,
                           ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.userSkillRepository = userSkillRepository;
        this.learningGoalRepository = learningGoalRepository;
        this.objectMapper = objectMapper;
    }

    public SkillGapDTO analyzeSkillGap(Long jobId, User user) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));

        List<UserSkill> userSkills = userSkillRepository.findByUser(user);
        Set<String> userSkillNames = userSkills.stream()
                .map(us -> us.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        List<JobSkill> jobSkills = job.getJobSkills() != null ? job.getJobSkills() : Collections.emptyList();

        List<String> matchingSkills = new ArrayList<>();
        List<SkillGapDTO.MissingSkillDetail> missingDetails = new ArrayList<>();

        for (JobSkill js : jobSkills) {
            String skillName = js.getSkill().getName();
            boolean isMandatory = Boolean.TRUE.equals(js.getIsRequired());

            if (userSkillNames.contains(skillName.toLowerCase())) {
                matchingSkills.add(skillName);
            } else {
                Optional<SkillDictionary.SkillMeta> meta = SkillDictionary.findSkill(skillName);
                List<String> topics = meta.map(SkillDictionary.SkillMeta::getTopics).orElse(
                        List.of("Core concepts of " + skillName, "Practical exercises & setup", "Best practices and implementation")
                );
                List<String> resources = meta.map(SkillDictionary.SkillMeta::getResources).orElse(
                        List.of("Official Documentation", "freeCodeCamp Tutorials", "Community Guides")
                );
                String category = meta.map(SkillDictionary.SkillMeta::getCategory).orElse("General");

                SkillGapDTO.MissingSkillDetail detail = new SkillGapDTO.MissingSkillDetail(
                        skillName, category, isMandatory, topics, resources
                );

                // Check existing learning goal progress if saved
                List<LearningGoal> existing = learningGoalRepository.findByUserIdAndTargetJobTitle(user.getId(), job.getTitle());
                existing.stream()
                        .filter(g -> g.getSkillName().equalsIgnoreCase(skillName))
                        .findFirst()
                        .ifPresent(g -> {
                            detail.setStatus(g.getStatus());
                            detail.setProgressPercentage(g.getProgressPercentage() != null ? g.getProgressPercentage() : 0);
                        });

                missingDetails.add(detail);
            }
        }

        // Sort missing: mandatory first
        missingDetails.sort((a, b) -> Boolean.compare(b.isMandatory(), a.isMandatory()));

        int total = jobSkills.size();
        double matchPct = total > 0 ? ((double) matchingSkills.size() / total) * 100.0 : 100.0;

        SkillGapDTO dto = new SkillGapDTO();
        dto.setJobId(job.getId());
        dto.setJobTitle(job.getTitle());
        dto.setCompany(job.getCompany());
        dto.setTotalRequiredSkills(total);
        dto.setMatchingSkillsCount(matchingSkills.size());
        dto.setMissingSkillsCount(missingDetails.size());
        dto.setMatchPercentage(Math.round(matchPct * 10.0) / 10.0);
        dto.setMatchingSkills(matchingSkills);
        dto.setMissingSkills(missingDetails);

        return dto;
    }

    @Transactional
    public LearningGoal saveOrUpdateGoal(User user, String skillName, String jobTitle, String status, Integer progress) {
        List<LearningGoal> goals = learningGoalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        LearningGoal goal = goals.stream()
                .filter(g -> g.getSkillName().equalsIgnoreCase(skillName) &&
                        (jobTitle == null || jobTitle.equalsIgnoreCase(g.getTargetJobTitle())))
                .findFirst()
                .orElse(new LearningGoal());

        goal.setUser(user);
        goal.setSkillName(skillName);
        if (jobTitle != null) goal.setTargetJobTitle(jobTitle);
        if (status != null) goal.setStatus(status);
        if (progress != null) goal.setProgressPercentage(progress);

        Optional<SkillDictionary.SkillMeta> meta = SkillDictionary.findSkill(skillName);
        if (meta.isPresent()) {
            goal.setCategory(meta.get().getCategory());
            try {
                goal.setRoadmapTopicsJson(objectMapper.writeValueAsString(meta.get().getTopics()));
                goal.setRecommendedResourcesJson(objectMapper.writeValueAsString(meta.get().getResources()));
            } catch (Exception ignored) {}
        }

        return learningGoalRepository.save(goal);
    }

    public List<LearningGoal> getUserGoals(User user) {
        return learningGoalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Transactional
    public void deleteGoal(Long goalId, User user) {
        learningGoalRepository.findById(goalId).ifPresent(g -> {
            if (g.getUser().getId().equals(user.getId())) {
                learningGoalRepository.delete(g);
            }
        });
    }
}
