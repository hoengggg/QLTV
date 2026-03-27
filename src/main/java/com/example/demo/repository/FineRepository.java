package com.example.demo.repository;

import com.example.demo.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByAmountBetween(Double min, Double max);
}
