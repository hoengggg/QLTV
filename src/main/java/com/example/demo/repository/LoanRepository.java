package com.example.demo.repository;

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
}
