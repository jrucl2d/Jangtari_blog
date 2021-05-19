package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories(){
        return categoryRepository.getAllCategories();
    }
    public Category addCategory(final CategoryDTO.Add categoryDTO) {
        final String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        return categoryRepository.save(categoryDTO.toEntity(pictureURL));
    }
    public Category updateCategory(final Long categoryId, final CategoryDTO.Update categoryDTO) {
        final Category category = findOne(categoryId);
        final String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        category.updateCategory(categoryDTO, pictureURL);
        return category;
    }
    public Category deleteCategory(final Long categoryId) {
        final Category category = findOne(categoryId);
        category.getDeleteFlag().softDelete();
        return category;
    }
    @Transactional(readOnly = true)
    public Category findOne(final Long categoryId) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchCategoryException());
        return category;
    }
}
