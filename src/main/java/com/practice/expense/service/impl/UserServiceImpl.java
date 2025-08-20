package com.practice.expense.service.impl;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.practice.expense.dao.UserRepository;
import com.practice.expense.entity.User;
import com.practice.expense.exception.AuthException;
import com.practice.expense.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;
	
	public UserServiceImpl() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public User validateUser(String email, String password) throws AuthException {
		
		if(email == null || password == null)
			throw new AuthException("Invalid credentials");
		
		email = email.toLowerCase();
		User user = userRepository.findByEmail(email);
		
		if(user == null)
			throw new AuthException("Invalid email address");
		
		if(!passwordEncoder.matches(password, user.getPassword())) 
			throw new AuthException("Incorrect password");
		
		return user;
	}
	
	@Override
	public User registerUser(User user) throws AuthException {
		
		if(user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null)
			throw new AuthException("Fill all the details");
		
		if(user.getFirstName() == "" || user.getLastName() == "" || user.getEmail() == "" || user.getPassword() == "")
			throw new AuthException("Fill all the details");
		
		// Validating email
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		user.setEmail(user.getEmail().toLowerCase());
		if(!pattern.matcher(user.getEmail()).matches()) 
			throw new AuthException("Invalid email format");
		
		// Check if if email is already in use
		if(userRepository.findByEmail(user.getEmail()) != null)
			throw new AuthException("Email already in use");

		// Encoding password
		//
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Integer id) throws AuthException {
		try {
			return userRepository.findById(id).orElseThrow(null);
		} catch (Exception e) {
			throw new AuthException("User doesn't exist");
		}
	}

}
