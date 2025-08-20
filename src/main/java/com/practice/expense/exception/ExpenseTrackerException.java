package com.practice.expense.exception;

public class ExpenseTrackerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public ExpenseTrackerException(String message) {
		super(message);
		this.message = message;
	}

	public ExpenseTrackerException(String string, Exception e) {
		// TODO Auto-generated constructor stub
			super(string, e);
			this.message = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
