package com.example.demo.repository;

import com.example.demo.dto.BlockUserDto;
import com.example.demo.dto.SumThanhToanDto;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContaining(String name, Pageable pageable);

    List<User> findByPenaltyBalanceBetween(Double min, Double max);

    @Query(value = """
        SELECT u.name AS TenKhachHang, SUM(p.amount) AS TongTienThanhToan FROM [User] u 
        LEFT JOIN Loan l ON l.user_id = u.id
        LEFT JOIN Fine f ON f.loan_id = l.id
        LEFT JOIN Payment p ON p.fine_id = f.id
        GROUP BY u.name
    """, nativeQuery = true)
    List<SumThanhToanDto> getAllThanhToan();

    @Query(value = """
        SELECT u.name AS TenNguoiDung, 
            CASE
                WHEN u.penaltyBalance > 0 THEN N'Nợ phạt'
                WHEN GETDATE() > m.endDate THEN N'Hết hạn membership'
                WHEN GETDATE() > m.endDate AND u.penaltyBalance > 0 THEN N'Vừa hết hạn vừa nợ'
                ELSE N'Vi phạm nội quy khác'
            END AS LyDo
         FROM [User] u 
        JOIN Membership m ON u.membership_id = m.id
        WHERE u.penaltyBalance > 0 OR GETDATE() > m.endDate
    """, nativeQuery = true)
    List<BlockUserDto> getAllBlockUser();
}
