package com.union.demo.repository;

import com.union.demo.entity.PostInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {

    //postId로 postInfo 찾아오기
    Optional<PostInfo> findByPostPostId(Long postId);
}
