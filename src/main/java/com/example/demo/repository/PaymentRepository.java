package com.example.demo.repository;

import com.example.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT DISTINCT a.method FROM Payment a")
    List<String> findDistinctMethodBy();

    @Query("SELECT DISTINCT a.status FROM Payment a")
    List<String> findDistinctStatusBy();

    List<Payment> findByMethodContaining(String method);

    List<Payment> findByAmountBetween(Double min, Double max);
}
