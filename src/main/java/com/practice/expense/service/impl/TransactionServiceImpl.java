// File: expense-tracker-api-main/expense-tracker-api-main/src/main/java/com/practice/expense/service/TransactionServiceImpl.java
package com.practice.expense.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.expense.dao.TransactionRepository;
import com.practice.expense.entity.Transaction;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.exception.ResourceNotFoundException;
import com.practice.expense.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId) {
        return transactionRepository.findByUserId(userId.longValue());
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer transactionId) throws ResourceNotFoundException {
        return transactionRepository.findByIdAndUserId(transactionId, userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws BadRequestException {
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new BadRequestException("Failed to save transaction");
        }
    }

    @Override
    public void removeTransactionById(Integer userId, Integer transactionId) throws ResourceNotFoundException {
        Transaction transaction = fetchTransactionById(userId, transactionId);
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction fetchTransactionById(int userId, Integer transactionId) {
        return transactionRepository.findByIdAndUserId(transactionId, (long) userId)
                .orElse(null);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws ResourceNotFoundException {
        return transactionRepository.findByIdAndUserIdAndCategoryId(transactionId, userId.longValue(), categoryId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }
}
