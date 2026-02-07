package com.union.demo.repository;

import com.union.demo.entity.PostInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {

    //저장하기
    //void save(PostInfo postInfo);

    //delete
    void deleteByPinfoId(Long postId);
}
