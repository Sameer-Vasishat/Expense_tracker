package com.practice.expense.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.practice.expense.dto.InvoiceRequestDTO;
import com.practice.expense.dto.InvoiceResponseDTO;
import com.practice.expense.entity.Transaction;
import com.practice.expense.entity.User;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.service.InvoiceProcessor;
import com.practice.expense.service.TransactionService;
import com.practice.expense.utilities.OCRService;
import com.practice.expense.utilities.Utils;

@Component("IMAGE")
public class ImageInvoiceProcessor implements InvoiceProcessor {

	@Autowired
	TransactionService transactionService;

	@Autowired
	OCRService ocrService;

	@Autowired
	Utils utils;
	

	public InvoiceResponseDTO process(InvoiceRequestDTO invoiceRequestDTO, User user) {

		String text = ocrService.extractText(invoiceRequestDTO.getImageData());
		System.out.println("OCR Result:\n" + text);

		// Step 2: Extract fields
		
		// Extract amount
		BigDecimal amount = BigDecimal.valueOf(utils.extractAmount(text));
		System.out.println("\u001B[1m" +  "\u001B[32m"+"****** Total amount extracted " + amount);
		
		//extract category
		String category = utils.extractCategory(text);
		System.out.println("****** Category extracted " + category);
		
		//extract invoice number
		String invoice_number = utils.extractInvoiceNumber(text);
		System.out.println("****** Invoice_number extracted: "+ invoice_number);
		
		LocalDateTime invoiceDate = utils.extractInvoiceDate(text);
		System.out.println("****** Invoice date extracted: "+ invoiceDate + "\u001B[0m");
		
		
		int categoryID = utils.getCategoryID(category);
		if (amount == null|| category == null) {
			throw new BadRequestException("Could not extract Values from invoice");
		}

		// Step 3: Save Transaction
		
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setAmount(amount);
		transaction.setDescription("Invoice Transaction - " + category);
		transaction.setCurrency("INR");
		transaction.setInvoiceNumber(invoice_number);
		transaction.setCreationDate(LocalDateTime.now());
		transaction.setInputType(invoiceRequestDTO.getInputType().name());
		transaction.setUserId((long)user.getId());
		transaction.setInvoiceDate(invoiceDate);
		transaction.setCategoryId((long)categoryID);
		Transaction saved = transactionService.saveTransaction(transaction);

		// Step 4: Response
		InvoiceResponseDTO response = new InvoiceResponseDTO();
		response.setId(saved.getId());
		response.setAmount(saved.getAmount());
		response.setDescription(saved.getDescription());
		response.setCategoryName(category);
		response.setInvoiceDate(invoiceDate);
		response.setInvoiceNumber(saved.getInvoiceNumber());
		return response;
	}

}

