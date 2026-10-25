package com.jobrec.service;

import com.jobrec.dto.SkillDTO;
import com.jobrec.dto.UserProfileDTO;
import com.jobrec.entity.Skill;
import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import com.jobrec.repository.SkillRepository;
import com.jobrec.repository.UserRepository;
import com.jobrec.repository.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;

    public UserProfileService(UserRepository userRepository,
                              SkillRepository skillRepository,
                              UserSkillRepository userSkillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    public UserProfileDTO getProfile(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setHeadline(user.getHeadline());
        dto.setLocation(user.getLocation());
        dto.setExperienceYears(user.getExperienceYears());
        dto.setPreferredWorkMode(user.getPreferredWorkMode());
        dto.setBio(user.getBio());

        List<UserSkill> skills = userSkillRepository.findByUser(user);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(us -> new SkillDTO(us.getSkill().getId(), us.getSkill().getName(),
                        us.getSkill().getCategory(), us.getProficiencyLevel()))
                .collect(Collectors.toList());
        dto.setSkills(skillDTOs);
        return dto;
    }

    @Transactional
    public UserProfileDTO updateProfile(User user, UserProfileDTO updateRequest) {
        if (updateRequest.getFullName() != null && !updateRequest.getFullName().isBlank()) {
            user.setFullName(updateRequest.getFullName().trim());
        }
        if (updateRequest.getHeadline() != null) user.setHeadline(updateRequest.getHeadline().trim());
        if (updateRequest.getLocation() != null) user.setLocation(updateRequest.getLocation().trim());
        if (updateRequest.getExperienceYears() != null) user.setExperienceYears(updateRequest.getExperienceYears());
        if (updateRequest.getPreferredWorkMode() != null) user.setPreferredWorkMode(updateRequest.getPreferredWorkMode());
        if (updateRequest.getBio() != null) user.setBio(updateRequest.getBio());

        user = userRepository.save(user);
        return getProfile(user);
    }

    @Transactional
    public UserProfileDTO addSkill(User user, String skillName, String proficiencyLevel) {
        if (skillName == null || skillName.isBlank()) {
            throw new IllegalArgumentException("Skill name cannot be empty");
        }

        Skill skill = skillRepository.findByNameIgnoreCase(skillName.trim()).orElseGet(() ->
                skillRepository.save(new Skill(skillName.trim(), "General"))
        );

        if (userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId()).isEmpty()) {
            UserSkill us = new UserSkill(user, skill, proficiencyLevel != null ? proficiencyLevel : "INTERMEDIATE");
            userSkillRepository.save(us);
        }

        return getProfile(user);
    }

    @Transactional
    public UserProfileDTO removeSkill(User user, Long skillId) {
        userSkillRepository.deleteByUserIdAndSkillId(user.getId(), skillId);
        return getProfile(user);
    }
}
