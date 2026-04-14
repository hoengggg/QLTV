package com.example.demo.repository;

import com.example.demo.dto.SachMuonNhieuDto;
import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SachMuonNhieuRepository extends JpaRepository<Book, Long> {
    @Query(value = """
        SELECT b.title AS TenSach, ld.quantity AS SoLuong FROM Book b 
        JOIN Loan_detail ld ON b.id = ld.book_id
        GROUP BY b.title, ld.quantity
        ORDER BY ld.quantity DESC 
    """, nativeQuery = true)
    List<SachMuonNhieuDto> getAllSachMuonNhieu();
}
