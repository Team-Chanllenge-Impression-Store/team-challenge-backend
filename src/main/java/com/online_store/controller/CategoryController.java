package com.online_store.controller;

import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.CategoryRequest;
import com.online_store.dto.response.CategoryResponse;
import com.online_store.dto.response.MessageResponse;
import com.online_store.entity.Category;
import com.online_store.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling {@link Category}-related HTTP requests.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(Path.CATEGORY)
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    /**
     * Handles the creation or updating of a category.
     * <p>
     * This method accepts a request body containing category details and either creates a new category or updates an
     * existing one based on the provided information. The method is accessible only to users with the 'ADMIN' authority.
     * </p>
     *
     * @param request The {@link CategoryRequest} object containing category details.
     * @return A {@link ResponseEntity} containing a {@link MessageResponse} indicating success and the appropriate HTTP status.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createOrUpdateCategory(request);
        return ResponseEntity.ok().body(new MessageResponse(String.valueOf(HttpStatus.OK),
                request.getId() == null ? SuccessMessage.CATEGORY_CREATED : SuccessMessage.CATEGORY_UPDATED));
    }

    /**
     * Retrieves a list of all categories.
     * <p>
     * This method returns a list of all categories available in the system.
     * </p>
     *
     * @return A {@link ResponseEntity} containing a list of {@link Category} objects.
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categories);
    }

    /**
     * Retrieves the details of a specific category.
     * <p>
     * This method accepts a category ID as a path variable and returns the details of the corresponding category.
     * </p>
     *
     * @param categoryId The ID of the category to retrieve.
     * @return A {@link ResponseEntity} containing a {@link CategoryResponse} with the category details.
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(new CategoryResponse(
                        category.getId(),
                        category.getDescription(),
                        category.getName(),
                        category.getProducts()
                )
        );
    }

}

