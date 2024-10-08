package com.online_store.web.product.service;

import com.online_store.web.product.dto.CategoryDTO;
import com.online_store.web.product.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category createOrUpdateCategory(CategoryDTO existingCategory);
}
