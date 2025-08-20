package com.practice.expense.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.expense.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public List<Category> findByUserId(Integer id);
	
	public Category findByIdAndUserId(Integer categoryId, Integer userId);
	
}
