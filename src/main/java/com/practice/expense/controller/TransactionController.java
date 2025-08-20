package com.practice.expense.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.expense.dto.InvoiceRequestDTO;
import com.practice.expense.dto.InvoiceResponseDTO;
import com.practice.expense.dto.InvoiceResponseListDTO;
import com.practice.expense.entity.Transaction;
import com.practice.expense.entity.User;
import com.practice.expense.exception.AuthException;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.exception.ExpenseTrackerException;
import com.practice.expense.factory.InvoiceProcessorFactory;
import com.practice.expense.service.InvoiceProcessor;
import com.practice.expense.service.TransactionService;
import com.practice.expense.service.UserService;

@RestController
@RequestMapping("/api")
public class TransactionController {

	 private final TransactionService transactionService;

	    // Constructor injection (recommended)
	    @Autowired
	    public TransactionController(TransactionService transactionService) {
	        this.transactionService = transactionService;
	    }

    
    @Autowired
    InvoiceProcessorFactory factory;

    @Autowired
    UserService userService;

    @GetMapping("expenses/transactions")
    public InvoiceResponseListDTO getAllTransactions(HttpServletRequest request) {
        try {
            int userId = (Integer) request.getAttribute("userId");
            List<Transaction> transactions = transactionService.fetchAllTransactions(userId);
            if (transactions.isEmpty()) {
				throw new BadRequestException("No transactions found for the user");
			}
            InvoiceResponseListDTO response = new InvoiceResponseListDTO();
            response.setTransactions(transactions.stream()
					.map(this::toInvoiceList)
					.collect(Collectors.toList()));
            response.setUserId(String.valueOf(userId));
            response.setMessage("Transactions fetched successfully");
			return response;
        }
        catch (BadRequestException e) {
        				throw e; // Re-throwing the specific exception
        }
        catch (Exception e) {
            throw new ExpenseTrackerException("Failed to fetch transactions", e);
        }
    }

    @GetMapping("expenses/transactions/{transactionId}")
    public InvoiceResponseDTO getTransactionById(HttpServletRequest request,
            @PathVariable("transactionId") Integer transactionId) {
        try {
            int userId = (Integer) request.getAttribute("userId");
            Transaction transaction = transactionService.fetchTransactionById(userId, transactionId);
            if (transaction == null) {
                throw new BadRequestException("Transaction not found");
            }
			return toInvoice(transaction);
        }
        catch (BadRequestException e) {
			throw e; // Re-throwing the specific exception
		}
        catch (Exception e) {
            throw new ExpenseTrackerException("Failed to fetch transaction", e);
        }
    }

    @PostMapping("expenses/transactions")
    public InvoiceResponseDTO addTransaction(HttpServletRequest request,
            @RequestBody InvoiceRequestDTO invoiceRequestDTO) {
        try {
            int userId = (Integer) request.getAttribute("userId");
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new AuthException("User not found");
            }
             InvoiceProcessor invoiceProcessor = factory.getProcessor(invoiceRequestDTO.getInputType());
             InvoiceResponseDTO invoiceResponseDTO = invoiceProcessor.process(invoiceRequestDTO, user);
            return invoiceResponseDTO;
        } 
        catch (BadRequestException e) {
        				throw e; // Re-throwing the specific exception
        }
        catch (Exception e) {
            throw new ExpenseTrackerException("Failed to add transaction", e);
        }
    }

    @PutMapping("expenses/transactions/{transactionId}")
    public InvoiceResponseDTO updateTransaction(HttpServletRequest request,
            @PathVariable("transactionId") Integer transactionId,
            @RequestBody InvoiceRequestDTO invoiceRequestDTO) {
        try {
            int userId = (Integer) request.getAttribute("userId");
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new AuthException("User not found");
            }
            InvoiceProcessor invoiceProcessor = factory.getProcessor(invoiceRequestDTO.getInputType());
            InvoiceResponseDTO invoiceResponseDTO = invoiceProcessor.process(invoiceRequestDTO, user);
            return invoiceResponseDTO;
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to update transaction", e);
        }
    }

    @DeleteMapping("expenses/transactions/{transactionId}")
    public String deleteTransaction(HttpServletRequest request,
            @PathVariable("transactionId") Integer transactionId) {
        try {
            int userId = (Integer) request.getAttribute("userId");
            transactionService.removeTransactionById(userId, transactionId);
            return "Deleted";
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to delete transaction", e);
        }
    }

   

    // Helper method to map Transaction to Invoice
    private InvoiceResponseDTO toInvoiceList(Transaction transaction) {
        InvoiceResponseDTO invoiceResponseDTO= new InvoiceResponseDTO(
				transaction.getAmount(),
				transaction.getDescription(),
			transaction.getId()
		);
        // Map other fields as needed
        return invoiceResponseDTO;
    }
    private InvoiceResponseDTO toInvoice(Transaction transaction) {
        InvoiceResponseDTO invoiceResponseDTO= new InvoiceResponseDTO(
				transaction.getAmount(),
				transaction.getDescription(),
			transaction.getId()
		);
        invoiceResponseDTO.setMessage("Transaction fetched successfully");
		invoiceResponseDTO.setCode("Success");
        // Map other fields as needed
        return invoiceResponseDTO;
    }
}