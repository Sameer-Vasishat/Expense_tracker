package com.practice.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Component
@Data
@JsonPropertyOrder({"id","invoiceNumber","CategoryName","amount","invoiceDate","description"})
public class InvoiceResponseDTO extends BaseResponse {
	
	public InvoiceResponseDTO(BigDecimal amount, String description, int id, String categoryName, String invoiceNumber,
			LocalDateTime invoiceDate) {
		super();
		this.amount = amount;
		this.description = description;
		this.id = id;
		CategoryName = categoryName;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
	}
	public InvoiceResponseDTO(BigDecimal amount, String description, int id) {
		super();
		this.amount = amount;
		this.description = description;
		this.id = id;
	}
	public InvoiceResponseDTO() {
		
	}
	BigDecimal amount;
	String description;
	int id;
	String CategoryName;
	String invoiceNumber;
	LocalDateTime invoiceDate;
	
}
