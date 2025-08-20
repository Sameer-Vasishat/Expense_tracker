package com.practice.expense.service;

import com.practice.expense.entity.User;
import com.practice.expense.exception.AuthException;

public interface UserService {

	public User validateUser(String email, String password) throws AuthException;
	
	public User registerUser(User user) throws AuthException;
	
	public User getUserById(Integer id) throws AuthException;
	
}
