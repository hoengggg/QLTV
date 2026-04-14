package com.example.demo.repository;

import com.example.demo.dto.DoanhThuDto;
import com.example.demo.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT DISTINCT a.method FROM Payment a")
    List<String> findDistinctMethodBy();

    @Query("SELECT DISTINCT a.status FROM Payment a")
    List<String> findDistinctStatusBy();

    Page<Payment> findByMethodContaining(String method, Pageable pageable);

    // Trong PaymentRepository.java
    Page<Payment> findByAmountBetween(Double min, Double max, Pageable pageable);

    @Query(value = """
       SELECT
            p.receiptNumber AS MaBienLai,
            u.name AS TenDocGia,
            p.amount AS SoTien,
            p.paymentDate AS NgayNop,
            p.method AS PhuongThuc,
            CASE
                WHEN f.reason = 0 THEN N'Quá hạn'
                ELSE N'Làm hỏng/Mất sách'
            END AS [Lý Do Phạt]
        FROM Payment p
        JOIN Fine f ON p.fine_id = f.id
        JOIN Loan l ON f.loan_id = l.id
        JOIN [User] u ON l.user_id = u.id
        WHERE p.status = 'Success' -- Chỉ tính các giao dịch đã thành công
        ORDER BY p.paymentDate DESC; 
    """, nativeQuery = true)
    List<DoanhThuDto> getAllDoanhThu();
}
