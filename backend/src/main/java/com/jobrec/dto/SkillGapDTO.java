package com.jobrec.dto;

import java.util.List;

public class SkillGapDTO {
    private Long jobId;
    private String jobTitle;
    private String company;
    private int totalRequiredSkills;
    private int matchingSkillsCount;
    private int missingSkillsCount;
    private double matchPercentage;
    private List<String> matchingSkills;
    private List<MissingSkillDetail> missingSkills;

    public static class MissingSkillDetail {
        private String skillName;
        private String category;
        private boolean isMandatory;
        private List<String> learningTopics;
        private List<String> recommendedResources;
        private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED
        private int progressPercentage;

        public MissingSkillDetail() {}

        public MissingSkillDetail(String skillName, String category, boolean isMandatory,
                                  List<String> learningTopics, List<String> recommendedResources) {
            this.skillName = skillName;
            this.category = category;
            this.isMandatory = isMandatory;
            this.learningTopics = learningTopics;
            this.recommendedResources = recommendedResources;
            this.status = "NOT_STARTED";
            this.progressPercentage = 0;
        }

        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public boolean isMandatory() { return isMandatory; }
        public void setMandatory(boolean mandatory) { isMandatory = mandatory; }

        public List<String> getLearningTopics() { return learningTopics; }
        public void setLearningTopics(List<String> learningTopics) { this.learningTopics = learningTopics; }

        public List<String> getRecommendedResources() { return recommendedResources; }
        public void setRecommendedResources(List<String> recommendedResources) { this.recommendedResources = recommendedResources; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getProgressPercentage() { return progressPercentage; }
        public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
    }

    public SkillGapDTO() {}

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public int getTotalRequiredSkills() { return totalRequiredSkills; }
    public void setTotalRequiredSkills(int totalRequiredSkills) { this.totalRequiredSkills = totalRequiredSkills; }

    public int getMatchingSkillsCount() { return matchingSkillsCount; }
    public void setMatchingSkillsCount(int matchingSkillsCount) { this.matchingSkillsCount = matchingSkillsCount; }

    public int getMissingSkillsCount() { return missingSkillsCount; }
    public void setMissingSkillsCount(int missingSkillsCount) { this.missingSkillsCount = missingSkillsCount; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchingSkills() { return matchingSkills; }
    public void setMatchingSkills(List<String> matchingSkills) { this.matchingSkills = matchingSkills; }

    public List<MissingSkillDetail> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<MissingSkillDetail> missingSkills) { this.missingSkills = missingSkills; }
}
