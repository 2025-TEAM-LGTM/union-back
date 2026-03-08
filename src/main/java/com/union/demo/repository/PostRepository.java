package com.union.demo.repository;

import com.union.demo.entity.Post;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    //postId로 post 찾기
    Optional<Post> findByPostId(Long postId);

    //공고 삭제 delete
    void deleteByPostId(Long postId);

    //상세 페이지 get : 여기서는 currentrole은 가져오지 않음
        @Query("""
    select distinct p
    from Post p
    left join fetch p.leaderId leader
    left join fetch p.postInfo pi
    left join fetch p.recruitRoles rr
    left join fetch rr.role r
    where p.postId= :postId

""")
    Optional<Post> findPostDetailWithRecruitRolesById(@Param("postId") Long postId);


}
