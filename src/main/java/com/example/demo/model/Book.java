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
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "ISBN")
    private String ISBN;

    @Column(name = "language")
    private String language;

    @Column(name = "edition")
    private String edition;

    @Column(name = "totalCopies")
    private Integer totalCopies;

    @Column(name = "availableCopies")
    private Integer availableCopies;

    @Column(name = "minLoanDays")
    private Integer minLoanDays;

    @Column(name = "maxLoanDays")
    private Integer maxLoanDays;

    @Column(name = "popularityScore")
    private Float popularityScore;

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    // --- THÊM VÀO: Mối quan hệ với Author qua bảng trung gian Book_author ---
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();



    // 2. SỬA: Dùng mappedBy trỏ đến biến "books" bên class Category
    // Không khai báo @JoinTable ở đây nữa vì đã khai báo bên Category rồi
    @ManyToMany(mappedBy = "books")
    private Set<Category> categories = new HashSet<>();
}
