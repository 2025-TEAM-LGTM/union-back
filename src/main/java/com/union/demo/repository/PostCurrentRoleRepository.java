package com.union.demo.repository;

import com.union.demo.entity.Post;
import com.union.demo.entity.PostCurrentRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCurrentRoleRepository extends JpaRepository<PostCurrentRole, Long> {

    //공고가 삭제되면 postCurrentRole도 같이 삭제됨
    void deleteByPost(Post post);

    //공고 상세 조회: current role 조회하기
    @Query("""
    select pcr
    from PostCurrentRole pcr
    join fetch pcr.role
    where pcr.post.postId = :postId
""")
    List<PostCurrentRole> findByPostIdWithCurrenRole(@Param("postId") Long postId);

    //현재 총 몇명인지 세기
    @Query("""
    select pcr.post.postId as postId, sum(pcr.count) as nowCount
    from PostCurrentRole pcr
    where pcr.post.postId in :postIds
    group by pcr.post.postId
""")
    List<PostNowCountRow> findNowCountByPostIds(@Param("postIds")List<Long> postIds);


}
