package com.example.demo.repository;

import com.example.demo.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Lấy danh sách các Action duy nhất
    @Query("SELECT DISTINCT a.action FROM AuditLog a")
    List<String> findDistinctActionBy();

    // Lấy danh sách các TargetType duy nhất
    @Query("SELECT DISTINCT a.targetType FROM AuditLog a")
    List<String> findDistinctTargetTypeBy();

    Page<AuditLog> findByDescriptionContaining(String description, Pageable pageable);
}
