package com.example.demo.repository;

import com.example.demo.model.Loan_detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanDetailRepository extends JpaRepository<Loan_detail, Long> {
}
