package com.jobrec.service;

import com.jobrec.dto.ApplicationDTO;
import com.jobrec.dto.ApplicationStatusUpdateRequest;
import com.jobrec.entity.Job;
import com.jobrec.entity.JobApplication;
import com.jobrec.entity.User;
import com.jobrec.repository.JobApplicationRepository;
import com.jobrec.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationTrackerService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationTrackerService(JobApplicationRepository applicationRepository,
                                     JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    public List<ApplicationDTO> getUserApplications(User user) {
        return applicationRepository.findByUserIdOrderByUpdatedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationDTO applyOrTrackJob(Long jobId, ApplicationStatusUpdateRequest request, User user) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));

        JobApplication app = applicationRepository.findByUserIdAndJobId(user.getId(), jobId)
                .orElseGet(() -> {
                    JobApplication newApp = new JobApplication();
                    newApp.setUser(user);
                    newApp.setJob(job);
                    newApp.setAppliedDate(LocalDate.now());
                    return newApp;
                });

        if (request != null) {
            if (request.getStatus() != null) {
                try {
                    app.setStatus(JobApplication.Status.valueOf(request.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    app.setStatus(JobApplication.Status.APPLIED);
                }
            }
            if (request.getAppliedDate() != null) app.setAppliedDate(request.getAppliedDate());
            if (request.getApplicationDeadline() != null) app.setApplicationDeadline(request.getApplicationDeadline());
            if (request.getInterviewDate() != null) app.setInterviewDate(request.getInterviewDate());
            if (request.getNotes() != null) app.setNotes(request.getNotes());
            if (request.getNextSteps() != null) app.setNextSteps(request.getNextSteps());
            if (request.getSalaryOffered() != null) app.setSalaryOffered(request.getSalaryOffered());
        }

        return toDTO(applicationRepository.save(app));
    }

    @Transactional
    public ApplicationDTO updateStatus(Long applicationId, ApplicationStatusUpdateRequest request, User user) {
        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + applicationId));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized to modify this application");
        }

        if (request.getStatus() != null) {
            app.setStatus(JobApplication.Status.valueOf(request.getStatus().toUpperCase()));
        }
        if (request.getAppliedDate() != null) app.setAppliedDate(request.getAppliedDate());
        if (request.getApplicationDeadline() != null) app.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getInterviewDate() != null) app.setInterviewDate(request.getInterviewDate());
        if (request.getNotes() != null) app.setNotes(request.getNotes());
        if (request.getNextSteps() != null) app.setNextSteps(request.getNextSteps());
        if (request.getSalaryOffered() != null) app.setSalaryOffered(request.getSalaryOffered());

        return toDTO(applicationRepository.save(app));
    }

    @Transactional
    public void deleteApplication(Long applicationId, User user) {
        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + applicationId));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized to delete this application");
        }

        applicationRepository.delete(app);
    }

    public Map<String, Long> getApplicationStats(User user) {
        Map<String, Long> stats = new LinkedHashMap<>();
        for (JobApplication.Status status : JobApplication.Status.values()) {
            stats.put(status.name(), applicationRepository.countByUserIdAndStatus(user.getId(), status));
        }
        return stats;
    }

    public ApplicationDTO toDTO(JobApplication app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setJobId(app.getJob().getId());
        dto.setJobTitle(app.getJob().getTitle());
        dto.setCompany(app.getJob().getCompany());
        dto.setLocation(app.getJob().getLocation());
        dto.setWorkMode(app.getJob().getWorkMode());
        dto.setStatus(app.getStatus().name());
        dto.setAppliedDate(app.getAppliedDate());
        dto.setApplicationDeadline(app.getApplicationDeadline());
        dto.setInterviewDate(app.getInterviewDate());
        dto.setNotes(app.getNotes());
        dto.setNextSteps(app.getNextSteps());
        dto.setSalaryOffered(app.getSalaryOffered());
        dto.setCreatedAt(app.getCreatedAt());
        dto.setUpdatedAt(app.getUpdatedAt());
        return dto;
    }
}
