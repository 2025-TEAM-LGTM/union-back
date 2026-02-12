package com.union.demo.repository;

import com.union.demo.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("""
select p
from Portfolio p
left join fetch p.domain d
left join fetch p.role r
left join fetch p.image i
where p.user.userId = :userId
order by p.portfolioId desc
""")
    List<Portfolio> findPortfolioByUserId(Long userId);
}
