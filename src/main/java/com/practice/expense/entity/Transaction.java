package com.practice.expense.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime invoiceDate;
    private String currency;
    private Long categoryId;
    private String metaInfo;
    private String invoiceNumber;
    private String status;
    private String inputType; // "userInput" or "bankStatement"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

}
