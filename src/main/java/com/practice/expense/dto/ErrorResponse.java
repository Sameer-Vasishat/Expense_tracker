package com.practice.expense.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    public ErrorResponse(String message, int value, long timeMillis) {
		// TODO Auto-generated constructor stub
    				this.message = message;
    				this.status = value;
    				this.timestamp = timeMillis;
	}
	private String message;
    private int status;
    private long timestamp;

    // Constructors, getters, setters
    
}
