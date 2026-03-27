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
@Table(name = "Fine")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "reason")
    private Boolean reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "issuedDate")
    private LocalDate issuedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;
}
