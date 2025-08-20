package com.practice.expense.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.expense.dao.CategoryRepository;
import com.practice.expense.entity.Category;
import com.practice.expense.exception.BadRequestException;
import com.practice.expense.exception.ResourceNotFoundException;
import com.practice.expense.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public List<Category> fetchCategoriesOfUser(Integer userId){
		return categoryRepository.findByUserId(userId);
	}

	@Override
	public Category fetchCategoryById(Integer categoryId, Integer userId) throws ResourceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category saveCategory(Category category) throws BadRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeCategoryById(Integer categoryId, Integer userId) throws ResourceNotFoundException {
		// TODO Auto-generated method stub
		
	}
}

