package com.union.demo.repository;

import com.union.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Users, Long>, MemberRepositoryCustom {

}
