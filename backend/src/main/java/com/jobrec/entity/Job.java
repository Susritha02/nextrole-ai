package com.jobrec.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "work_mode", nullable = false, length = 50)
    private String workMode = "REMOTE"; // REMOTE, HYBRID, ONSITE

    @Column(name = "employment_type", nullable = false, length = 50)
    private String employmentType = "FULL_TIME"; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    @Column(name = "min_salary", precision = 10, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 10, scale = 2)
    private BigDecimal maxSalary;

    @Column(name = "salary_currency", length = 10)
    private String salaryCurrency = "USD";

    @Column(name = "min_experience_years")
    private Double minExperienceYears = 0.0;

    @Column(name = "max_experience_years")
    private Double maxExperienceYears = 5.0;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String eligibility;

    @Column(name = "is_demo")
    private Boolean isDemo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JobSkill> jobSkills = new ArrayList<>();

    public Job() {}

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

    public List<JobSkill> getJobSkills() { return jobSkills; }
    public void setJobSkills(List<JobSkill> jobSkills) { this.jobSkills = jobSkills; }
}
