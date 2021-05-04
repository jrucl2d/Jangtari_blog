package com.yu.jangtari.controller;

import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.service.CategoryService;
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
    public List<CategoryDTO.Get> getAllCategory() {
        return categoryService.getAllCategories().stream().map(category -> CategoryDTO.Get.of(category)).collect(Collectors.toList());
    }
    @PostMapping(value = "/admin/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO.Get addCategory(final CategoryDTO.Add categoryDTO) {
        return CategoryDTO.Get.of(categoryService.addCategory(categoryDTO));
    }
    @PutMapping("/admin/category")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO.Get updateCategory(@RequestBody @Valid final CategoryDTO.Update categoryDTO) {
        return CategoryDTO.Get.of(categoryService.updateCategory(categoryDTO));
    }
    @DeleteMapping("/admin/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCategory(@PathVariable(value = "id") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return "";
    }
}