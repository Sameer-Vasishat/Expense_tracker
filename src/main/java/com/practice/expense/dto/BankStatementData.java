package com.practice.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class BankStatementData {
    
    private String bankName;
    private String accountNumber;
    private LocalDate transactionDate;
    private String transactionDescription;
    private BigDecimal amount;
    private String currency;
    private String referenceNumber;

    public BankStatementData() {
    }

    public BankStatementData(String bankName, String accountNumber, LocalDate transactionDate,
                              String transactionDescription, BigDecimal amount, String currency, String referenceNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.amount = amount;
        this.currency = currency;
        this.referenceNumber = referenceNumber;
    }

}
