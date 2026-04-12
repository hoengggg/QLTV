package com.example.demo.repository;

import com.example.demo.dto.MuonQuaHanDto;
import com.example.demo.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT DISTINCT a.status FROM Loan a")
    List<String> findDistinctStatusBy();

    Page<Loan> findByStatusContaining(String status, Pageable pageable);

    List<Loan> findByLoanDateBetween(LocalDate start, LocalDate end);

    //------------------------------------------------------------------------------------------------

    @Query(value = """
    SELECT u.name AS tenNguoiDung, b.title AS tenSach, l.overdueDays AS soNgayTre FROM Loan l 
        JOIN [User] u ON l.user_id = u.id 
        JOIN Loan_detail ld ON l.id = ld.loan_id
        JOIN Book b ON ld.book_id = b.id
    """, nativeQuery = true)
    List<MuonQuaHanDto> getAllMuonQuaHan();
}
