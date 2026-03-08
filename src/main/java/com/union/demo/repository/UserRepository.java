package com.union.demo.repository;

import com.union.demo.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <Users,Long>{
    //username 중복 조회
    Boolean existsByUsername(String username);

    //로그인용
    Optional<Users> findByLoginId(String loginId);

    //회원가입시 중복체크
    boolean existsByLoginId(String loginId);

    //userId로 user 찾기
    Optional<Users> findByUserId(Long userId);

    //memberMatch
    @Query("""
    select distinct u
    from Users u
    left join fetch  u.image
    left join fetch u.mainRoleId
    left join fetch u.userSkills us
    left join fetch us.skill
    where u.userId in :ids
""")
    List<Users> findAllByUserIdInWithDetail(@Param("ids")List<Long> ids);

}
