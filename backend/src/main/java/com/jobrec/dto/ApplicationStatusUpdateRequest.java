package com.jobrec.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationStatusUpdateRequest {
    private String status;
    private LocalDate appliedDate;
    private LocalDate applicationDeadline;
    private LocalDateTime interviewDate;
    private String notes;
    private String nextSteps;
    private BigDecimal salaryOffered;

    public ApplicationStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public LocalDateTime getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDateTime interviewDate) { this.interviewDate = interviewDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getNextSteps() { return nextSteps; }
    public void setNextSteps(String nextSteps) { this.nextSteps = nextSteps; }

    public BigDecimal getSalaryOffered() { return salaryOffered; }
    public void setSalaryOffered(BigDecimal salaryOffered) { this.salaryOffered = salaryOffered; }
}
