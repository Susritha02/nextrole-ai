package com.jobrec.repository;

import com.jobrec.entity.Job;
import com.jobrec.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    List<JobSkill> findByJob(Job job);
    List<JobSkill> findByJobId(Long jobId);
}
