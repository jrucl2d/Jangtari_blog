package com.yu.jangtari.service;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO.Get> getAllCategories(){
        return categoryRepository.getAllCategories();
    }

    @Transactional
    public Long addCategory(String newCategory, MultipartFile categoryImageFile) throws CustomException {
        Category category = new Category();

        if(newCategory == null){
            throw new CustomException("입력 정보가 충분하지 않습니다.", "카테고리 추가 실패");
        }
        category.setName(newCategory);
        if(categoryImageFile != null){
            System.out.println("새로운 이미지 -----------------------");
            System.out.println(categoryImageFile);
        }
//        Category saved = categoryRepository.save(category);
//        return saved.getId();
    return 1L;
    }

    @Transactional
    public void updateCategory(CategoryDTO.Update theCategory) throws CustomException{
        if(theCategory.getId() == null || theCategory.getName() == null) {
            throw new CustomException("입력 정보가 충분하지 않습니다.", "카테고리 수정 실패 : id = " + theCategory.getId());
        }
        Optional<Category> category = categoryRepository.findById(theCategory.getId());
        if(category.isPresent()){
            category.get().setName(theCategory.getName());

        } else {
            throw new CustomException("존재하지 않는 카테고리입니다.", "카테고리 수정 실패 : id = " + theCategory.getId());
        }
    }

    public void deleteCategory(Long theId) throws CustomException {
        try{
            categoryRepository.deleteById(theId);
        } catch (Exception e){
            throw new CustomException("존재하지 않는 카테고리입니다.", "카테고리 삭제 실패 : id = " + theId);
        }
    }
}
