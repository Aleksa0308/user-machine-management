package com.raf.usermanagement.repositories;

import com.raf.usermanagement.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Set<Permission> findByNameIn(Set<String> permissionNames);
    Permission findByName(String permissionName);
}
