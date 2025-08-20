// File: expense-tracker-api-main/expense-tracker-api-main/src/main/java/com/practice/expense/repository/TransactionRepository.java
package com.practice.expense.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practice.expense.entity.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Long userId);

    Optional<Transaction> findByIdAndUserId(Integer transactionId, Long userId);

    Optional<Transaction> findByIdAndUserIdAndCategoryId(Integer transactionId, Long userId, Long categoryId);
}
