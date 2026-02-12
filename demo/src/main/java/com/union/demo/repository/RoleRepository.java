package com.union.demo.repository;

import com.union.demo.entity.Field;
import com.union.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional <Role> findByRoleId(long roleId);

    List<Role> findByField(long fieldId);

    //role로 field 찾기
    @Query(""" 
select r.field
from Role r
where r.roleId = :roleId
""")
    Optional<Field> findFieldByRoleId(@Param("roleId") Integer roleId);
}
