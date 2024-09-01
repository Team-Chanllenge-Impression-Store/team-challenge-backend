package com.online_store.service;

import com.online_store.dto.request.CategoryRequest;
import com.online_store.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category createOrUpdateCategory(CategoryRequest existingCategory);
}
