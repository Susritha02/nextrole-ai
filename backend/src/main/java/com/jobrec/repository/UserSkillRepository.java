package com.jobrec.repository;

import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);
    Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId);
    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}
