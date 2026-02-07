package com.union.demo.repository;

import com.union.demo.entity.Post;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //getPosts: post 리스트 목록 조회 및 검색조회
    @Query("""
        select distinct p
        from Post p
        left join fetch p.postInfo pi
        left join fetch p.recruitRoles prr
        left join fetch prr.role r 
        where 
            (:roleIds is null or r.roleId in :roleIds)
              and (
                   :domainIds is null
                   or p.primeDomainId.domainId in :domainIds
                   or p.secondDomainId.domainId in :domainIds
              )
            and(pi.status = 'OPEN' )
        order by p.postId desc
    """)
    List<Post> findAllWithInfoAndRecruitRoles(
            @Param("domainIds") List<Integer> domainIds,
            @Param("fieldIds") List<Integer> fieldIds,
            @Param("roleIds") List<Integer> roleIds
    );

    //공고 작성 create


    //공고 삭제 delete

    void deleteByPostId(Long postId);


}
