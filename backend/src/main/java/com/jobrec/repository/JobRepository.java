package com.jobrec.repository;

import com.jobrec.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    @Query("SELECT DISTINCT j FROM Job j " +
           "LEFT JOIN j.jobSkills js " +
           "LEFT JOIN js.skill s " +
           "WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:workMode IS NULL OR LOWER(j.workMode) = LOWER(:workMode)) " +
           "AND (:employmentType IS NULL OR LOWER(j.employmentType) = LOWER(:employmentType)) " +
           "AND (:maxExp IS NULL OR j.minExperienceYears <= :maxExp)")
    List<Job> searchJobs(
        @Param("keyword") String keyword,
        @Param("workMode") String workMode,
        @Param("employmentType") String employmentType,
        @Param("maxExp") Double maxExp
    );
}
