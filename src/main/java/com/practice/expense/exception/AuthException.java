package com.practice.expense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.UNAUTHORIZED)
// Custom exception for authentication errors
public class AuthException extends RuntimeException {

	public AuthException(String message) {
		super(message);
	}
	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}
}
