package com.online_store.service.impl;

import com.online_store.constants.ErrorMessage;
import com.online_store.dto.request.CategoryRequest;
import com.online_store.entity.Category;
import com.online_store.entity.Product;
import com.online_store.repository.CategoryRepository;
import com.online_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            logger.warn("Categories table is empty. No categories found: {}", categories);
        }
        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public Category createOrUpdateCategory(CategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            logger.error("Category name is null or empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.INVALID_CATEGORY_NAME);
        }

        Category category;
        if (request.getId() != null) {
            // Update existing category
            category = categoryRepository.findById(request.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CATEGORY_NOT_FOUND));
        } else {
            // Create new category
            category = new Category();
        }
        // Update fields
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setProducts(request.getProducts() == null ? new ArrayList<Product>() : request.getProducts());
        categoryRepository.save(category);
        logger.info("Saved category: {}", category);
        return category;
    }
}
