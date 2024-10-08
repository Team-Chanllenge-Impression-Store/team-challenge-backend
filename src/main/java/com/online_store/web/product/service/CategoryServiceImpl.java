package com.online_store.web.product.service;

import com.online_store.utils.constant.ErrorMessage;
import com.online_store.web.product.dao.CategoryRepository;
import com.online_store.web.product.dto.CategoryDTO;
import com.online_store.web.product.model.Category;
import com.online_store.web.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.warn("Categories table is empty. No categories found: {}", categories);
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
    public Category createOrUpdateCategory(CategoryDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            log.error("Category name is null or empty");
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
        log.info("Saved category: {}", category);
        return category;
    }
}
