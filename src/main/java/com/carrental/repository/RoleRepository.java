package com.carrental.repository;

import com.carrental.entity.Role;
import com.carrental.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM roles r WHERE r.name = :roleName", nativeQuery = true)
    Optional<Role> findByNameNative(@Param("roleName") String roleName);

    @Query(value = "SELECT COUNT(*) > 0 FROM roles WHERE name = :roleName", nativeQuery = true)
    boolean existsByNameNative(@Param("roleName") String roleName);

    Optional<Role> findByName(RoleName name);
}