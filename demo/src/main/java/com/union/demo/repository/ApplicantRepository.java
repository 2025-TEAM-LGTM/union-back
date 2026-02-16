package com.union.demo.repository;

import com.union.demo.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    //중복 지원 방지용
    boolean existsByPost_PostIdAndUser_UserId(Long postId, Long userId);

}
