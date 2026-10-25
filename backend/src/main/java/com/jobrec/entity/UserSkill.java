package com.jobrec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "skill_id"})
})
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "proficiency_level", length = 30)
    private String proficiencyLevel = "INTERMEDIATE"; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserSkill() {}

    public UserSkill(User user, Skill skill, String proficiencyLevel) {
        this.user = user;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel != null ? proficiencyLevel : "INTERMEDIATE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
