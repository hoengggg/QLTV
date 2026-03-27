package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "averageLoanDays")
    private Integer averageLoanDays;

    @Column(name = "totalBooks")
    private Integer totalBooks;

    // --- THÊM VÀO: Kết nối ngược lại với Book ---
    @ManyToMany(fetch = FetchType.EAGER) // Dùng EAGER để không bị Null khi hiển thị danh sách
    @JoinTable(
            name = "Book_category", // Phải khớp đúng tên bảng trong SQL của bạn
            joinColumns = @JoinColumn(name = "category_id"), // Khóa ngoại bảng Category
            inverseJoinColumns = @JoinColumn(name = "book_id") // Khóa ngoại bảng Book
    )
    private Set<Book> books = new HashSet<>();
}
