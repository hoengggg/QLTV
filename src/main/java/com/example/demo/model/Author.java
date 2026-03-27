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
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "activeYears")
    private String activeYears;

    @Column(name = "awardsCount")
    private Integer awardsCount;

    // --- THÊM VÀO: Kết nối ngược lại với Book ---
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
