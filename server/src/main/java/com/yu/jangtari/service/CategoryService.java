package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.util.GoogleDriveUtil;
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
    private final PostService postService;

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
        category.updateCategory(categoryDTO);
        return category;
    }
    // Category와 연관된 Post 모두 삭제 처리
    public Category deleteCategory(final Long categoryId) {
        final Category category = findOne(categoryId);
        category.getDeleteFlag().softDelete();
        postService.deletePostsOfCategory(categoryId);
        return category;
    }
    @Transactional(readOnly = true)
    public Category findOne(final Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(NoSuchCategoryException::new);
    }
}
