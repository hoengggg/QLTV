package com.example.demo.repository;

import com.example.demo.dto.MemberShipActiveDto;
import com.example.demo.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByStartDateBetween(LocalDate start, LocalDate end);

    @Query(value = """
        SELECT u.name AS TenNguoiDung, m.endDate AS NgayHetHan FROM [User] u 
        JOIN Membership m ON u.membership_id = m.id WHERE m.status = 1 AND m.endDate > GETDATE()
    """, nativeQuery = true)
    List<MemberShipActiveDto> getAllMember();
}
