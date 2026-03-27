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
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "paymentDate")
    private LocalDate paymentDate;

    @Column(name = "method")
    private String method;

    @Column(name = "receiptNumber")
    private String receiptNumber;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "fine_id", referencedColumnName = "id")
    private Fine fine;
}
