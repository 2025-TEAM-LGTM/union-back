package com.union.demo.repository;

import com.union.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional <Role> findByRoleId(long roleId);

    List<Role> findByFieldId(long fieldId);
}
