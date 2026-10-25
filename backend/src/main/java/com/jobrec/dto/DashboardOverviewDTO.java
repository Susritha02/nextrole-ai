package com.jobrec.dto;

import java.util.List;
import java.util.Map;

public class DashboardOverviewDTO {
    private long totalJobsCount;
    private long savedJobsCount;
    private long totalApplicationsCount;
    private long interviewCount;
    private long offerCount;
    private Map<String, Long> applicationsByStatus;
    private List<RecommendationResultDTO> topRecommendations;
    private List<ApplicationDTO> recentApplications;
    private List<SkillDTO> userSkills;
    private Double profileExperienceYears;

    public DashboardOverviewDTO() {}

    public long getTotalJobsCount() { return totalJobsCount; }
    public void setTotalJobsCount(long totalJobsCount) { this.totalJobsCount = totalJobsCount; }

    public long getSavedJobsCount() { return savedJobsCount; }
    public void setSavedJobsCount(long savedJobsCount) { this.savedJobsCount = savedJobsCount; }

    public long getTotalApplicationsCount() { return totalApplicationsCount; }
    public void setTotalApplicationsCount(long totalApplicationsCount) { this.totalApplicationsCount = totalApplicationsCount; }

    public long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(long interviewCount) { this.interviewCount = interviewCount; }

    public long getOfferCount() { return offerCount; }
    public void setOfferCount(long offerCount) { this.offerCount = offerCount; }

    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }

    public List<RecommendationResultDTO> getTopRecommendations() { return topRecommendations; }
    public void setTopRecommendations(List<RecommendationResultDTO> topRecommendations) { this.topRecommendations = topRecommendations; }

    public List<ApplicationDTO> getRecentApplications() { return recentApplications; }
    public void setRecentApplications(List<ApplicationDTO> recentApplications) { this.recentApplications = recentApplications; }

    public List<SkillDTO> getUserSkills() { return userSkills; }
    public void setUserSkills(List<SkillDTO> userSkills) { this.userSkills = userSkills; }

    public Double getProfileExperienceYears() { return profileExperienceYears; }
    public void setProfileExperienceYears(Double profileExperienceYears) { this.profileExperienceYears = profileExperienceYears; }
}
