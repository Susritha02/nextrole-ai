package com.jobrec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_goals")
public class LearningGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;

    @Column(length = 50)
    private String category = "General";

    @Column(name = "target_job_title", length = 150)
    private String targetJobTitle;

    @Column(name = "roadmap_topics_json", columnDefinition = "TEXT")
    private String roadmapTopicsJson;

    @Column(name = "recommended_resources_json", columnDefinition = "TEXT")
    private String recommendedResourcesJson;

    @Column(length = 50)
    private String status = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, COMPLETED

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public LearningGoal() {}

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTargetJobTitle() { return targetJobTitle; }
    public void setTargetJobTitle(String targetJobTitle) { this.targetJobTitle = targetJobTitle; }

    public String getRoadmapTopicsJson() { return roadmapTopicsJson; }
    public void setRoadmapTopicsJson(String roadmapTopicsJson) { this.roadmapTopicsJson = roadmapTopicsJson; }

    public String getRecommendedResourcesJson() { return recommendedResourcesJson; }
    public void setRecommendedResourcesJson(String recommendedResourcesJson) { this.recommendedResourcesJson = recommendedResourcesJson; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
