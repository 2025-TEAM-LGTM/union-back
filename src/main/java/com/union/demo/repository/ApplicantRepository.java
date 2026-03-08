package com.union.demo.repository;

import com.union.demo.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    //중복 지원 방지용
    boolean existsByPost_PostIdAndUser_UserId(Long postId, Long userId);

    //지원여부
    @Query("""
    select a.post.postId
    from Applicant a
    where a.user.userId = :userId and a.post.postId in :postIds

""")
    List<Long> findAppliedPostIds(@Param("userId") Long userId,
                                  @Param("postIds") List<Long> postIds);
    Optional<Applicant> findByPost_PostIdAndUser_UserId(Long postId, Long userId);

    void deleteByPost_PostIdAndUser_UserId(Long postId, Long userId);

    //post에 지원한 지원자들 찾기
    @Query("""
    select a.user.userId
    from Applicant a
    where a.post.postId= :postId
    """)
    List<Long> findApplicantUserId(@Param("postId")Long postId);
}
