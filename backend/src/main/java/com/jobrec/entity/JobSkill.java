package com.jobrec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "job_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_id", "skill_id"})
})
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "weight")
    private Double weight = 1.0;

    public JobSkill() {}

    public JobSkill(Job job, Skill skill, Boolean isRequired, Double weight) {
        this.job = job;
        this.skill = skill;
        this.isRequired = isRequired != null ? isRequired : true;
        this.weight = weight != null ? weight : 1.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}
