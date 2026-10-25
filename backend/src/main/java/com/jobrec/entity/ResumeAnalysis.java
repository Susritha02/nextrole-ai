package com.jobrec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_analyses")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "parsed_text", columnDefinition = "LONGTEXT")
    private String parsedText;

    @Column(name = "detected_skills_json", columnDefinition = "TEXT")
    private String detectedSkillsJson; // JSON array string e.g. ["Java", "React"]

    @Column(name = "detected_experience_years")
    private Double detectedExperienceYears = 0.0;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    @Column(name = "analyzed_at", updatable = false)
    private LocalDateTime analyzedAt = LocalDateTime.now();

    public ResumeAnalysis() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getParsedText() { return parsedText; }
    public void setParsedText(String parsedText) { this.parsedText = parsedText; }

    public String getDetectedSkillsJson() { return detectedSkillsJson; }
    public void setDetectedSkillsJson(String detectedSkillsJson) { this.detectedSkillsJson = detectedSkillsJson; }

    public Double getDetectedExperienceYears() { return detectedExperienceYears; }
    public void setDetectedExperienceYears(Double detectedExperienceYears) { this.detectedExperienceYears = detectedExperienceYears; }

    public String getProfileSummary() { return profileSummary; }
    public void setProfileSummary(String profileSummary) { this.profileSummary = profileSummary; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}
