package com.example.demo.repository;

import com.example.demo.model.Payment;
import com.example.demo.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Page<Publisher> findByNameContaining(String name, Pageable pageable);

    List<Publisher> findByAverageLoanDaysBetween(Double min, Double max);
}
