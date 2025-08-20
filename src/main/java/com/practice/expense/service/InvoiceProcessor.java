package com.practice.expense.service;

import org.springframework.stereotype.Service;

import com.practice.expense.dto.InvoiceRequestDTO;
import com.practice.expense.dto.InvoiceResponseDTO;
import com.practice.expense.entity.User;
@Service
public interface InvoiceProcessor {
	InvoiceResponseDTO process(InvoiceRequestDTO invoiceRequestDTO, User user);
}
