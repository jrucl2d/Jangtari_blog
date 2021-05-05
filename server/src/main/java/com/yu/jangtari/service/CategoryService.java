package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRepositoryQuerydsl categoryRepositoryQuerydsl;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories(){
        return categoryRepositoryQuerydsl.getAllCategories();
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchCategoryException());
        return category;
    }
    public Category addCategory(CategoryDTO.Add categoryDTO) {
        String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        return categoryRepository.save(categoryDTO.toEntity(pictureURL));
    }
    public Category updateCategory(Long categoryId, CategoryDTO.Update categoryDTO) {
        final Category category = getCategory(categoryId);
        String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        category.updateCategory(categoryDTO, pictureURL);
        return category;
    }
    public Category deleteCategory(Long categoryId) {
        final Category category = getCategory(categoryId);
        category.getDeleteFlag().softDelete();
        return category;
    }
}
