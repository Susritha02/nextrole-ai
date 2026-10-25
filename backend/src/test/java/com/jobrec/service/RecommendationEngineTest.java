package com.jobrec.service;

import com.jobrec.dto.RecommendationResultDTO;
import com.jobrec.entity.Job;
import com.jobrec.entity.JobSkill;
import com.jobrec.entity.Skill;
import com.jobrec.entity.User;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationEngineTest {

    private JobRepository jobRepository;
    private UserSkillRepository userSkillRepository;
    private JobService jobService;
    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        jobRepository = Mockito.mock(JobRepository.class);
        userSkillRepository = Mockito.mock(UserSkillRepository.class);
        jobService = Mockito.mock(JobService.class);
        recommendationEngine = new RecommendationEngine(jobRepository, userSkillRepository, jobService);
    }

    @Test
    void testEvaluateJobMatchHighSkillOverlap() {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Full Stack Developer");
        job.setDescription("Building web apps with Java and React");
        job.setMinExperienceYears(1.0);
        job.setMaxExperienceYears(3.0);
        job.setWorkMode("REMOTE");
        job.setMinSalary(BigDecimal.valueOf(80000));
        job.setMaxSalary(BigDecimal.valueOf(100000));

        Skill s1 = new Skill("Java", "Languages");
        Skill s2 = new Skill("React", "Frameworks");
        JobSkill js1 = new JobSkill(job, s1, true, 1.0);
        JobSkill js2 = new JobSkill(job, s2, true, 1.0);
        job.setJobSkills(List.of(js1, js2));

        User user = new User();
        user.setId(10L);
        user.setExperienceYears(2.0);
        user.setPreferredWorkMode("REMOTE");
        user.setHeadline("Java React Engineer");

        Set<String> userSkills = Set.of("java", "react");

        RecommendationResultDTO result = recommendationEngine.evaluateJobMatch(job, user, userSkills);

        assertNotNull(result);
        assertTrue(result.getMatchPercentage() >= 80.0, "Matching both primary skills should yield high score (> 80%)");
        assertEquals(2, result.getMatchingSkills().size());
        assertTrue(result.getMissingSkills().isEmpty());
        assertNotNull(result.getExplanation());
    }

    @Test
    void testEvaluateJobMatchZeroSkillOverlap() {
        Job job = new Job();
        job.setId(2L);
        job.setTitle("DevOps Kubernetes Admin");
        job.setDescription("Managing AWS and Kubernetes clusters");
        job.setMinExperienceYears(3.0);
        job.setMaxExperienceYears(5.0);
        job.setWorkMode("ONSITE");

        Skill s1 = new Skill("Kubernetes", "Cloud & DevOps");
        JobSkill js1 = new JobSkill(job, s1, true, 1.0);
        job.setJobSkills(List.of(js1));

        User user = new User();
        user.setId(11L);
        user.setExperienceYears(0.5);
        user.setPreferredWorkMode("REMOTE");

        Set<String> userSkills = Set.of("python");

        RecommendationResultDTO result = recommendationEngine.evaluateJobMatch(job, user, userSkills);

        assertNotNull(result);
        assertTrue(result.getMatchPercentage() < 60.0, "Missing required skills and insufficient experience should produce low match");
        assertTrue(result.getMatchingSkills().isEmpty());
        assertEquals(1, result.getMissingSkills().size());
    }
}
