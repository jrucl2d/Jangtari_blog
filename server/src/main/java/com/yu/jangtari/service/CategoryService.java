package com.yu.jangtari.service;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public int addCategory(CategoryDTO.Add newCategory) {
        Category category = new Category();

        if(newCategory.getName() == null || newCategory.getPicture() == null){
            return 0;
        }
        category.setName(newCategory.getName());
        if(!newCategory.getPicture().equals("")){
            category.setPicture(newCategory.getPicture());
        }
        categoryRepository.save(category);
        return 1;
    }

    public List<CategoryDTO.Get> getAllCategories(){
        return categoryRepository.getAllCategories();
    }
}
