package com.practice.expense.service.impl;

import org.springframework.stereotype.Component;

import com.practice.expense.dto.InvoiceRequestDTO;
import com.practice.expense.dto.InvoiceResponseDTO;
import com.practice.expense.entity.User;
import com.practice.expense.service.InvoiceProcessor;
@Component("BANK_STATEMENT")
public class BankStatementInvoiceProcessor implements InvoiceProcessor {


	@Override
	public InvoiceResponseDTO process(InvoiceRequestDTO invoiceRequestDTO, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
