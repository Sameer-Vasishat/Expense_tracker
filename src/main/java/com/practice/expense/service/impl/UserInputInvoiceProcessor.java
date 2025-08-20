package com.practice.expense.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.practice.expense.dto.InvoiceRequestDTO;
import com.practice.expense.dto.InvoiceResponseDTO;
import com.practice.expense.dto.UserInputData;
import com.practice.expense.entity.Transaction;
import com.practice.expense.entity.User;
import com.practice.expense.service.InvoiceProcessor;
import com.practice.expense.service.TransactionService;

@Component("USER")
@Primary
public class UserInputInvoiceProcessor implements InvoiceProcessor {

	@Autowired
	TransactionService transactionService;

	@Override
	public InvoiceResponseDTO process(InvoiceRequestDTO invoiceRequestDTO, User user) {
		// convert invoiceRequestDTO to transaction entity
		// and save it to the database
		InvoiceResponseDTO invoiceResponseDTO = new InvoiceResponseDTO();
		if (invoiceRequestDTO.getInputType() == InvoiceRequestDTO.InputType.USER) {
			// create covnersion logic here
			
			Transaction transaction = convertToTransaction(invoiceRequestDTO.getUserInput(), user);
			//uthrowing "this.transactionService" is null
			
			transactionService.saveTransaction(transaction);
			
			// For now, just return a dummy response
			invoiceResponseDTO= new InvoiceResponseDTO(
					transaction.getAmount(),
					transaction.getDescription(),
				transaction.getId()
			);
			invoiceResponseDTO.setMessage("Transaction added successfully");
			invoiceResponseDTO.setCode("Success");
		}
		return invoiceResponseDTO;
	}

	private Transaction convertToTransaction(UserInputData userInput, User user) {
		
		// Implement the logic to convert UserInputData to Transaction entity
		Transaction transaction = new Transaction();
		transaction.setUser(user);
			
		transaction.setAmount(userInput.getAmount());
		transaction.setCategoryId(userInput.getCategoryId());
		transaction.setDescription(userInput.getDescription());
		transaction.setCreationDate(userInput.getInvoiceDate());
		transaction.setInputType("USER");
		transaction.setCategoryId(userInput.getCategoryId());
		transaction.setCurrency(userInput.getCurrency());
		transaction.setInvoiceNumber(userInput.getInvoiceNumber());
		transaction.setUserId((long)user.getId());
		// Add any other fields that are necessary for the Transaction entity
		return transaction;
	}

}
