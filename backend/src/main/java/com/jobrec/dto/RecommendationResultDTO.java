package com.jobrec.dto;

import java.util.List;

public class RecommendationResultDTO {
    private JobDTO job;
    private double matchPercentage;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> requiredSkills;
    private String experienceCompatibility;
    private String locationCompatibility;
    private double skillScore;
    private double experienceScore;
    private double workModeScore;
    private String explanation;

    public RecommendationResultDTO() {}

    public JobDTO getJob() { return job; }
    public void setJob(JobDTO job) { this.job = job; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchingSkills() { return matchingSkills; }
    public void setMatchingSkills(List<String> matchingSkills) { this.matchingSkills = matchingSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getExperienceCompatibility() { return experienceCompatibility; }
    public void setExperienceCompatibility(String experienceCompatibility) { this.experienceCompatibility = experienceCompatibility; }

    public String getLocationCompatibility() { return locationCompatibility; }
    public void setLocationCompatibility(String locationCompatibility) { this.locationCompatibility = locationCompatibility; }

    public double getSkillScore() { return skillScore; }
    public void setSkillScore(double skillScore) { this.skillScore = skillScore; }

    public double getExperienceScore() { return experienceScore; }
    public void setExperienceScore(double experienceScore) { this.experienceScore = experienceScore; }

    public double getWorkModeScore() { return workModeScore; }
    public void setWorkModeScore(double workModeScore) { this.workModeScore = workModeScore; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
