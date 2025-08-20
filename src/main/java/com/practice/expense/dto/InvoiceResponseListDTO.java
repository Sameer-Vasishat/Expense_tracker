package com.practice.expense.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Component
@Data
@JsonPropertyOrder({ "userId", "message", "transactions" })
public class InvoiceResponseListDTO extends BaseResponse {
	
	public InvoiceResponseListDTO() {
		// TODO Auto-generated constructor stub
	}
	String UserId;
	String message;
	List<InvoiceResponseDTO> transactions;
	
	
	
}
