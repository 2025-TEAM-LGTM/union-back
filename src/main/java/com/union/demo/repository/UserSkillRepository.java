package com.union.demo.repository;

import com.union.demo.entity.UserSkill;
import com.union.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    @Query("""
select us
from UserSkill us
join fetch us.skill s
join fetch us.user u
where u.userId in :userIds
""")
    List<UserSkill> findAllByUserIdInWithSkill(List<Long> userIds);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from UserSkill us where us.user.userId = :userId")
    void deleteByUser_UserId(Long userId);

    List<Long> user(Users user);
}
