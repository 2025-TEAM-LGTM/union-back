package com.union.demo.repository;

import com.union.demo.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UniversityRepository extends JpaRepository<University,Long> {
    Optional<University> findByUnivId(Long univId);

    // 이름에 특정 키워드가 포함된 대학 목록 검색
    List<University> findByUnivNameContaining(String keyword);

}
