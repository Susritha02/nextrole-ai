package com.jobrec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JobDTO {
    private Long id;
    private String title;
    private String company;
    private String location;
    private String workMode;
    private String employmentType;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String salaryCurrency;
    private Double minExperienceYears;
    private Double maxExperienceYears;
    private String description;
    private String responsibilities;
    private String eligibility;
    private Boolean isDemo;
    private LocalDateTime createdAt;
    private List<String> requiredSkills;
    private List<String> optionalSkills;
    private Boolean isSaved = false;
    private String applicationStatus;

    public JobDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public BigDecimal getMinSalary() { return minSalary; }
    public void setMinSalary(BigDecimal minSalary) { this.minSalary = minSalary; }

    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }

    public String getSalaryCurrency() { return salaryCurrency; }
    public void setSalaryCurrency(String salaryCurrency) { this.salaryCurrency = salaryCurrency; }

    public Double getMinExperienceYears() { return minExperienceYears; }
    public void setMinExperienceYears(Double minExperienceYears) { this.minExperienceYears = minExperienceYears; }

    public Double getMaxExperienceYears() { return maxExperienceYears; }
    public void setMaxExperienceYears(Double maxExperienceYears) { this.maxExperienceYears = maxExperienceYears; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public Boolean getIsDemo() { return isDemo; }
    public void setIsDemo(Boolean isDemo) { this.isDemo = isDemo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public List<String> getOptionalSkills() { return optionalSkills; }
    public void setOptionalSkills(List<String> optionalSkills) { this.optionalSkills = optionalSkills; }

    public Boolean getIsSaved() { return isSaved; }
    public void setIsSaved(Boolean isSaved) { this.isSaved = isSaved; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
}
