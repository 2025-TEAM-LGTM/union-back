package com.union.demo.repository;

import com.union.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
