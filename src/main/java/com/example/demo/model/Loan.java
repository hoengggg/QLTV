package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "loanDate")
    private LocalDate loanDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dueDate")
    private LocalDate dueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "returnDate")
    private LocalDate returnDate;

    @Column(name = "status")
    private String status;

    @Column(name = "totalBooks")
    private Integer totalBooks;

    @Column(name = "overdueDays")
    private Integer overdueDays;

    @Column(name = "fineAmount")
    private Double fineAmount;

    @Column(name = "reminderSent")
    private Boolean reminderSent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
