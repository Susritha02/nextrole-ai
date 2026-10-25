package com.jobrec.service;

import com.jobrec.dto.JobDTO;
import com.jobrec.dto.JobFilterRequest;
import com.jobrec.entity.Job;
import com.jobrec.entity.JobApplication;
import com.jobrec.entity.SavedJob;
import com.jobrec.entity.User;
import com.jobrec.repository.JobApplicationRepository;
import com.jobrec.repository.JobRepository;
import com.jobrec.repository.SavedJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository,
                      SavedJobRepository savedJobRepository,
                      JobApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<JobDTO> getJobs(JobFilterRequest filter, User currentUser) {
        String keyword = filter != null && filter.getKeyword() != null && !filter.getKeyword().isBlank()
                ? filter.getKeyword().trim() : null;
        String workMode = filter != null && filter.getWorkMode() != null && !filter.getWorkMode().equalsIgnoreCase("ALL")
                ? filter.getWorkMode().trim() : null;
        String employmentType = filter != null && filter.getEmploymentType() != null && !filter.getEmploymentType().equalsIgnoreCase("ALL")
                ? filter.getEmploymentType().trim() : null;
        Double maxExp = filter != null && filter.getMaxExp() != null && filter.getMaxExp() > 0
                ? filter.getMaxExp() : null;

        List<Job> jobs = jobRepository.searchJobs(keyword, workMode, employmentType, maxExp);

        // Sorting
        if (filter != null && filter.getSortBy() != null) {
            switch (filter.getSortBy().toLowerCase()) {
                case "salary":
                    jobs.sort((a, b) -> {
                        double sA = a.getMaxSalary() != null ? a.getMaxSalary().doubleValue() : 0.0;
                        double sB = b.getMaxSalary() != null ? b.getMaxSalary().doubleValue() : 0.0;
                        return Double.compare(sB, sA);
                    });
                    break;
                case "newest":
                default:
                    jobs.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
                    break;
            }
        }

        return jobs.stream().map(job -> toDTO(job, currentUser)).collect(Collectors.toList());
    }

    public JobDTO getJobById(Long id, User currentUser) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + id));
        return toDTO(job, currentUser);
    }

    @Transactional
    public boolean saveJob(Long jobId, User user) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + jobId));

        if (!savedJobRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            savedJobRepository.save(new SavedJob(user, job));
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unsaveJob(Long jobId, User user) {
        if (savedJobRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            savedJobRepository.deleteByUserIdAndJobId(user.getId(), jobId);
            return true;
        }
        return false;
    }

    public List<JobDTO> getSavedJobs(User user) {
        List<SavedJob> saved = savedJobRepository.findByUserIdOrderBySavedAtDesc(user.getId());
        return saved.stream().map(s -> toDTO(s.getJob(), user)).collect(Collectors.toList());
    }

    public JobDTO toDTO(Job job, User user) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setCompany(job.getCompany());
        dto.setLocation(job.getLocation());
        dto.setWorkMode(job.getWorkMode());
        dto.setEmploymentType(job.getEmploymentType());
        dto.setMinSalary(job.getMinSalary());
        dto.setMaxSalary(job.getMaxSalary());
        dto.setSalaryCurrency(job.getSalaryCurrency());
        dto.setMinExperienceYears(job.getMinExperienceYears());
        dto.setMaxExperienceYears(job.getMaxExperienceYears());
        dto.setDescription(job.getDescription());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setEligibility(job.getEligibility());
        dto.setIsDemo(job.getIsDemo());
        dto.setCreatedAt(job.getCreatedAt());

        // Skills split into required and optional
        List<String> requiredSkills = new ArrayList<>();
        List<String> optionalSkills = new ArrayList<>();
        if (job.getJobSkills() != null) {
            job.getJobSkills().forEach(js -> {
                if (Boolean.TRUE.equals(js.getIsRequired())) {
                    requiredSkills.add(js.getSkill().getName());
                } else {
                    optionalSkills.add(js.getSkill().getName());
                }
            });
        }
        dto.setRequiredSkills(requiredSkills);
        dto.setOptionalSkills(optionalSkills);

        if (user != null) {
            dto.setIsSaved(savedJobRepository.existsByUserIdAndJobId(user.getId(), job.getId()));
            Optional<JobApplication> app = applicationRepository.findByUserIdAndJobId(user.getId(), job.getId());
            dto.setApplicationStatus(app.map(a -> a.getStatus().name()).orElse(null));
        }

        return dto;
    }
}
