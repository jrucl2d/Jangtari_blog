package com.yu.jangtari.api.category.repository;

import com.yu.jangtari.api.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CustomCategoryRepository {
    List<Category> getAllCategories();
    Optional<Category> getCategoryForDelete(Long categoryId);
}
