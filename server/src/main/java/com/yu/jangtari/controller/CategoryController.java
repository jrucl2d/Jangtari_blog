package com.yu.jangtari.controller;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @PostMapping("/addCategory")
    public ResponseEntity<List<CategoryDTO.Get>> addCategory(@RequestBody CategoryDTO.Add newCategory){

        Category category = new Category();

        if(newCategory.getName() == null || newCategory.getPicture() == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        category.setName(newCategory.getName());
        if(newCategory.getPicture() != ""){
            category.setPicture(newCategory.getPicture());
        }
        categoryRepository.save(category);

        List<CategoryDTO.Get> allCategories = new ArrayList<>();
        categoryRepository.findAll().forEach(v -> {
            allCategories.add(new CategoryDTO.Get(v.getId(), v.getName(), v.getPicture()));
        });
        return new ResponseEntity<>(allCategories, HttpStatus.CREATED);
    }
}
