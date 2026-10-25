package com.jobrec.repository;

import com.jobrec.entity.LearningGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal, Long> {
    List<LearningGoal> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<LearningGoal> findByUserIdAndTargetJobTitle(Long userId, String targetJobTitle);
    void deleteByUserIdAndSkillName(Long userId, String skillName);
}
