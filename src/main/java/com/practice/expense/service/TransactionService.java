package com.practice.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.practice.expense.entity.Transaction;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.exception.ResourceNotFoundException;
@Service
public interface TransactionService {
	
	public List<Transaction> fetchAllTransactions(Integer userId);
	
	public Transaction fetchTransactionById(Integer userId, Integer transactionId) throws ResourceNotFoundException;

	public Transaction saveTransaction(Transaction transaction) throws BadRequestException;
	
	public void removeTransactionById(Integer userId, Integer categoryId) throws ResourceNotFoundException;

	public Transaction fetchTransactionById(int userId, Integer transactionId);

	Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
			throws ResourceNotFoundException;
}
