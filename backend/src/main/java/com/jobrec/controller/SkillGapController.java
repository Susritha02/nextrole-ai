package com.jobrec.controller;

import com.jobrec.dto.SkillGapDTO;
import com.jobrec.entity.LearningGoal;
import com.jobrec.entity.User;
import com.jobrec.service.AuthService;
import com.jobrec.service.SkillGapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skill-gap")
public class SkillGapController {

    private final SkillGapService skillGapService;
    private final AuthService authService;

    public SkillGapController(SkillGapService skillGapService, AuthService authService) {
        this.skillGapService = skillGapService;
        this.authService = authService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<SkillGapDTO> getSkillGapForJob(@PathVariable Long jobId) {
        User user = authService.getCurrentAuthenticatedUser();
        SkillGapDTO dto = skillGapService.analyzeSkillGap(jobId, user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/goal")
    public ResponseEntity<LearningGoal> saveOrUpdateGoal(@RequestBody Map<String, Object> payload) {
        User user = authService.getCurrentAuthenticatedUser();
        String skillName = (String) payload.get("skillName");
        String jobTitle = (String) payload.get("jobTitle");
        String status = (String) payload.get("status");
        Integer progress = payload.get("progressPercentage") != null ?
                ((Number) payload.get("progressPercentage")).intValue() : 0;

        LearningGoal goal = skillGapService.saveOrUpdateGoal(user, skillName, jobTitle, status, progress);
        return ResponseEntity.ok(goal);
    }

    @GetMapping("/goals")
    public ResponseEntity<List<LearningGoal>> getUserGoals() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(skillGapService.getUserGoals(user));
    }

    @DeleteMapping("/goal/{id}")
    public ResponseEntity<Map<String, Object>> deleteGoal(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        skillGapService.deleteGoal(id, user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
