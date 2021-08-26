package com.yu.jangtari.api.category.controller;

import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto.Get> getAllCategories() {
        return categoryService.getAllCategories().stream().map(CategoryDto.Get::of).collect(Collectors.toList());
    }
    @PostMapping(value = "/admin/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto.Get addCategory(@Valid CategoryDto.Add categoryDTO) {
        return CategoryDto.Get.of(categoryService.addCategory(categoryDTO));
    }
    @PostMapping(value = "/admin/category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto.Get updateCategory(@PathVariable("id") Long categoryId, @Valid CategoryDto.Update categoryDTO) {
        return CategoryDto.Get.of(categoryService.updateCategory(categoryId, categoryDTO));
    }
    @DeleteMapping("/admin/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCategory(@PathVariable(value = "id") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return "OK";
    }
}