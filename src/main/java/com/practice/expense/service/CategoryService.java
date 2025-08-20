package com.practice.expense.service;

import java.util.List;

import com.practice.expense.exception.BadRequestException;
import com.practice.expense.exception.ResourceNotFoundException;
import com.practice.expense.entity.Category;

public interface CategoryService {

	public List<Category> fetchCategoriesOfUser(Integer userId);
	
	public Category fetchCategoryById(Integer categoryId, Integer userId) throws ResourceNotFoundException;
	
	public Category saveCategory(Category category) throws BadRequestException;
	
	public void removeCategoryById(Integer categoryId, Integer userId) throws ResourceNotFoundException;
}
