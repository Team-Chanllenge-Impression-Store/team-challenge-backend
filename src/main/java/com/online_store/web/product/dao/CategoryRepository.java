package com.online_store.web.product.dao;

import com.online_store.web.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryById(Long categoryId);
}
