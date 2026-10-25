package com.jobrec.repository;

import com.jobrec.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {
    List<ResumeAnalysis> findByUserIdOrderByAnalyzedAtDesc(Long userId);
    Optional<ResumeAnalysis> findFirstByUserIdOrderByAnalyzedAtDesc(Long userId);
}
