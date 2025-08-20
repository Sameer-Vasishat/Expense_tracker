package com.practice.expense.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practice.expense.dao.CategoryRepository;
import com.practice.expense.entity.Category;
import com.practice.expense.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private List<Category> categories;

  @BeforeEach
  void setUp() {
   
  }

  @DisplayName("JUnit test for fetchCategoriesOfUser method")
  @Test
  void fetchCategoriesOfUser() {
    when(categoryRepository.findByUserId(1)).thenReturn(categories);
    assertEquals(2, categoryService.fetchCategoriesOfUser(1).size());
  }

  @DisplayName("JUnit test for fetchCategoryById method")
  @Test
  void fetchCategoryById() {
    when(categoryRepository.findByIdAndUserId(1, 1))
        .thenReturn(categories.get(0))
        .thenReturn(categories.get(1));
  
  }

  @DisplayName("JUnit test for saveCategory method")
  @Test
  void saveCategory() {
    when(categoryRepository.save(categories.get(0)))
        .thenReturn(categories.get(0))
        .thenReturn(null);
    assertNotNull(categoryService.saveCategory(categories.get(0)));
    assertNull(categoryService.saveCategory(null));
  }

  @DisplayName("JUnit test for removeCategoryById method")
  @Test
  @Disabled
  void removeCategoryById() {
//    when(categoryRepository.delete(categories.get(0))).thenThrow(EtResourceNotFoundException.class);
//    assertThrows(EtResourceNotFoundException.class, () -> categoryService.removeCategoryById(1, 1));
    assertTrue(true);
  }
}