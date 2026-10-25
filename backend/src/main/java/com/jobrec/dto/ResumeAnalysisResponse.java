package com.jobrec.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeAnalysisResponse {
    private Long id;
    private String fileName;
    private List<String> detectedSkills;
    private Double detectedExperienceYears;
    private String profileSummary;
    private String disclaimer = "Automated analysis is heuristic-based and may be imperfect. Review and update your skills in Profile as needed.";
    private LocalDateTime analyzedAt;

    public ResumeAnalysisResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public List<String> getDetectedSkills() { return detectedSkills; }
    public void setDetectedSkills(List<String> detectedSkills) { this.detectedSkills = detectedSkills; }

    public Double getDetectedExperienceYears() { return detectedExperienceYears; }
    public void setDetectedExperienceYears(Double detectedExperienceYears) { this.detectedExperienceYears = detectedExperienceYears; }

    public String getProfileSummary() { return profileSummary; }
    public void setProfileSummary(String profileSummary) { this.profileSummary = profileSummary; }

    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}
