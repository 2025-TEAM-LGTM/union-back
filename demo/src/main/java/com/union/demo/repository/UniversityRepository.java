package com.union.demo.repository;

import com.union.demo.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University,Long> {
    Optional<University> findByUnivId(Long univId);
}
