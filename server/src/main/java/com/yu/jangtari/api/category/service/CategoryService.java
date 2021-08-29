package com.yu.jangtari.api.category.service;

import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public List<CategoryDto.Get> getAllCategories(){
        List<Category> categories = categoryRepository.getAllCategories();
        return CategoryDto.Get.toList(categories);
    }

    public CategoryDto.Get addCategory(CategoryDto.Add categoryDto, MultipartFile picture) {
        Category savedCategory = categoryRepository.save(categoryDto.toEntity(googleDriveUtil, picture));
        return CategoryDto.Get.of(savedCategory);
    }

    public CategoryDto.Get updateCategory(CategoryDto.Update categoryDto, MultipartFile picture) {
        Category category = getOne(categoryDto.getCategoryId());
        return CategoryDto.Get.of(category.updateCategory(categoryDto.toUrlDto(googleDriveUtil, picture)));
    }

    @Transactional(readOnly = true)
    public Category getOne(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_ERROR));
    }

    public void deleteCategory(Long categoryId) {
        Category category = getOneForDelete(categoryId);
        category.softDelete();
    }

    private Category getOneForDelete(Long categoryId) {
        return categoryRepository.findCategoryForDelete(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_ERROR));
    }
}
