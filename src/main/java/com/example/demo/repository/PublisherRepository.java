package com.example.demo.repository;

import com.example.demo.model.Payment;
import com.example.demo.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByNameContaining(String name);

    List<Publisher> findByAverageLoanDaysBetween(Double min, Double max);
}
