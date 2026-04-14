package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "[User]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "membershipLevel")
    private Boolean membershipLevel;

    @Column(name = "maxLoanLimit")
    private Integer maxLoanLimit;

    @Column(name = "currentLoanCount")
    private Integer currentLoanCount;

    @Column(name = "overdueCount")
    private Integer overdueCount;

    @Column(name = "reservationLimit")
    private Integer reservationLimit;

    @Column(name = "penaltyBalance")
    private Double penaltyBalance;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "membership_id", referencedColumnName = "id")
    private Membership membership;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;
}
