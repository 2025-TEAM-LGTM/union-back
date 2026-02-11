package com.union.demo.repository;

import com.union.demo.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findAllBySkillIdIn(Set<Integer> skillId);
}
