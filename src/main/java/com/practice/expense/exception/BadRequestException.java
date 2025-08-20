package com.practice.expense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String string, Exception e) {
		// TODO Auto-generated constructor stub
		super(string, e);
		
	}
}
