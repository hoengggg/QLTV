package com.example.demo.repository;

import com.example.demo.dto.ThongKeHoatDongDto;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThongKeHoatDongRepository extends JpaRepository<User, Long> {
    @Query(value = """
    SELECT u.name AS TenNguoiDung, u.status AS Status, u.currentLoanCount AS SachMuon FROM [User] u WHERE u.status = 'Active' """, nativeQuery = true)
    List<ThongKeHoatDongDto> getAllThongKeHoatDong();
}
