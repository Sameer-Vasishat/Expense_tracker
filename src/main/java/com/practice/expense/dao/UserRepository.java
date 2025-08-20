package com.practice.expense.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.expense.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByEmail(String email);
}
