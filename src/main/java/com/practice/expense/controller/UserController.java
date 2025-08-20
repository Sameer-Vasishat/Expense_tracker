package com.practice.expense.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.expense.Constants;
import com.practice.expense.entity.User;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api")
public class UserController {
	//change this to constructor injection for better practice
	// and to avoid circular dependencies
	
	
	UserService userService;
	@Autowired
			public UserController(UserService userService) {
				this.userService = userService;
	}
	
	
	
	
	/**
	 * This method is used to login a user. It validates the user credentials and
	 * generates a JWT token if the credentials are valid.
	 * 
	 * @param user The user object containing email and password.
	 * @return A map containing the JWT token.
	 */
	@PostMapping("users/login")
	public Map<String, String> loginUser(@RequestBody User user) {
		User existingUser = userService.validateUser(user.getEmail(), user.getPassword());
		return generateJWTToken(existingUser);
	}
	
	/**
	 * This method is used to register a new user. It creates a new user in the
	 * database and generates a JWT token for the user.
	 * 
	 * @param user The user object containing first name, last name, email, and
	 *             password.
	 * @return A map containing the JWT token.
	 */

	@PostMapping("users/register")
	public Map<String, String> registerUser(@RequestBody User user) {
		User newUser = userService.registerUser(user);
	
		return generateJWTToken(newUser);
	}
	
	/**
	 * This method generates a JWT token for the user. The token contains user
	 * details and is signed with a secret key.
	 * 
	 * @param user The user object containing user details.
	 * @return A map containing the JWT token.
	 */
	private Map<String, String> generateJWTToken(User user) {
		long timeStamp = System.currentTimeMillis();
		String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
				.setIssuedAt(new Date(timeStamp))
				.setExpiration(new Date(timeStamp + Constants.TOKEN_VALIDITY))
				.claim("userId", user.getId())
				.claim("firstName", user.getFirstName())
				.claim("lastName", user.getLastName())
				.claim("email", user.getEmail())
				.compact();
		//to be stored in the database for future reference
		Map<String, String> map = new HashMap<>();
		map.put("Token", token);
		map.put("UserId", String.valueOf(user.getId()));
		map.put("Message","Token generated successfully");
		
		return map;
	}
}
