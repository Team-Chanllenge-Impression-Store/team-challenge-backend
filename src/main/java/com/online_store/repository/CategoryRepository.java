package com.online_store.repository;

import com.online_store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryById(Long categoryId);
}
