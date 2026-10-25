package com.jobrec.repository;

import com.jobrec.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserIdOrderByUpdatedAtDesc(Long userId);
    Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, JobApplication.Status status);
}
