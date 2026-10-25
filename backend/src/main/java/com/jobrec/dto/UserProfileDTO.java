package com.jobrec.dto;

import java.util.List;

public class UserProfileDTO {
    private Long id;
    private String fullName;
    private String email;
    private String headline;
    private String location;
    private Double experienceYears;
    private String preferredWorkMode;
    private String bio;
    private List<SkillDTO> skills;

    public UserProfileDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Double experienceYears) { this.experienceYears = experienceYears; }

    public String getPreferredWorkMode() { return preferredWorkMode; }
    public void setPreferredWorkMode(String preferredWorkMode) { this.preferredWorkMode = preferredWorkMode; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<SkillDTO> getSkills() { return skills; }
    public void setSkills(List<SkillDTO> skills) { this.skills = skills; }
}
