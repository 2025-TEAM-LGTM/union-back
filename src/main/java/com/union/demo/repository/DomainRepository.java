package com.union.demo.repository;

import com.union.demo.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain,Integer> {
    //domainId로 검색
    Optional<Domain> findByDomainId(Integer domainId);
}
