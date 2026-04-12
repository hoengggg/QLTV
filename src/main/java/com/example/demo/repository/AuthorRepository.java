package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findByNameContaining(String name, Pageable pageable);

    List<Author> findByAwardsCountBetween(Integer min, Integer max);
}
