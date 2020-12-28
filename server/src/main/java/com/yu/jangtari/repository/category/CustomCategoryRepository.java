package com.yu.jangtari.repository.category;

import com.yu.jangtari.domain.DTO.CategoryDTO;

import java.util.List;

public interface CustomCategoryRepository {
    public List<CategoryDTO.Get> getAllCategories();
}
