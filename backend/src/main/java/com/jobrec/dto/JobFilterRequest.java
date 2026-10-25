package com.jobrec.dto;

public class JobFilterRequest {
    private String keyword;
    private String workMode;
    private String employmentType;
    private Double maxExp;
    private String sortBy; // newest, salary, relevance

    public JobFilterRequest() {}

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public Double getMaxExp() { return maxExp; }
    public void setMaxExp(Double maxExp) { this.maxExp = maxExp; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
