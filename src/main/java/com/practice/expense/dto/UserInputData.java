package com.practice.expense.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class UserInputData {
    
    private String invoiceNumber;
    private String customerName;
    private LocalDateTime invoiceDate;
    private BigDecimal amount;
    private String currency;
    private String description;
    private Long categoryId; // Added category ID

 
    public UserInputData(String invoiceNumber, String customerName, LocalDateTime invoiceDate,
                         BigDecimal amount, String currency, String description, Long categoryId) {
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.categoryId = categoryId;
    }

   
}

