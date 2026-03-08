package com.union.demo.repository;

import com.union.demo.entity.Post;
import com.union.demo.entity.PostRecruitRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecruitRoleRepository extends JpaRepository<PostRecruitRole, Long> {
    void deleteByPost(Post post);
}
