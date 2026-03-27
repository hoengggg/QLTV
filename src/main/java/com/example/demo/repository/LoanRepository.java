package com.example.demo.repository;

import com.example.demo.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT DISTINCT a.status FROM Loan a")
    List<String> findDistinctStatusBy();

    List<Loan> findByStatusContaining(String status);

    List<Loan> findByLoanDateBetween(LocalDate start, LocalDate end);
}
