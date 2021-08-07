package com.yu.jangtari.api.category.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.util.GoogleDriveUtil;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.dto.CategoryDTO;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostService postService;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories(){
        return categoryRepository.getAllCategories();
    }
    public Category addCategory(final CategoryDTO.Add categoryDTO) {
        final String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        categoryDTO.setPictureURL(pictureURL);
        return categoryRepository.save(categoryDTO.toEntity());
    }
    public Category updateCategory(final Long categoryId, final CategoryDTO.Update categoryDTO) {
        final Category category = findOne(categoryId);
        final String pictureURL = googleDriveUtil.fileToURL(categoryDTO.getPicture(), GDFolder.CATEGORY);
        categoryDTO.setPictureURL(pictureURL);
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
