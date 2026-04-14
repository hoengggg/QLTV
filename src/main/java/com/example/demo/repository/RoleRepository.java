package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Trong RoleRepository.java
    Page<Role> findByPermissionLevelBetween(Integer min, Integer max, Pageable pageable);
}
