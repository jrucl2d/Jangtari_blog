package com.yu.jangtari.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
//import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
//    private final CategoryRepositoryQuerydsl categoryRepositoryQuerydsl;
    private final GoogleDriveUtil googleDriveUtil;

//    @Transactional(readOnly = true)
//    public List<Category> getAllCategories(){
//        return categoryRepositoryQuerydsl.getAllCategories();
//    }
//
//    @Transactional
//    public Long addCategory(String newCategory, MultipartFile categoryImageFile) throws CustomException, GeneralSecurityException, IOException {
//        Category category = new Category();
//
//        if(newCategory == null){
//            throw new CustomException("입력 정보가 충분하지 않습니다.", "카테고리 추가 실패");
//        }
//        category.setName(newCategory);
//        if(categoryImageFile != null){
//            Drive drive = googleDriveUtil.getDrive();
//            File file = new File();
//            file.setName(googleDriveUtil.getPictureName(newCategory));
//            file.setParents(Collections.singletonList(googleDriveUtil.CATEGORY_FOLDER));
//            java.io.File tmpFile = googleDriveUtil.convert(categoryImageFile);
//            FileContent content = new FileContent("image/jpeg", tmpFile);
//            File uploadedFile = drive.files().create(file, content).setFields("id").execute();
//            tmpFile.delete();
//            String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
//            category.setPicture(fileRef);
//        }
//        Category saved = categoryRepository.save(category);
//        return saved.getId();
//        return 1L;
//    }
//
//    @Transactional
//    public void updateCategory(CategoryDTO.Update theCategory, MultipartFile categoryImageFile) throws CustomException, GeneralSecurityException, IOException {
//        if(theCategory.getId() == null || theCategory.getName() == null) {
//            throw new CustomException("입력 정보가 충분하지 않습니다.", "카테고리 수정 실패 : id = " + theCategory.getId());
//        }
//        Optional<Category> category = categoryRepository.findById(theCategory.getId());
//        if(category.isPresent()){
//            category.get().setName(theCategory.getName());
//            if(categoryImageFile != null){
//                Drive drive = googleDriveUtil.getDrive();
//                File file = new File();
//                file.setName(googleDriveUtil.getPictureName(theCategory.getName()));
//                file.setParents(Collections.singletonList(googleDriveUtil.CATEGORY_FOLDER));
//                java.io.File tmpFile = googleDriveUtil.convert(categoryImageFile);
//                FileContent content = new FileContent("image/jpeg", tmpFile);
//                File uploadedFile = drive.files().create(file, content).setFields("id").execute();
//                tmpFile.delete();
//                String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
//                category.get().setPicture(fileRef);
//            }
//            categoryRepository.save(category.get());
//        } else {
//            throw new CustomException("존재하지 않는 카테고리입니다.", "카테고리 수정 실패 : id = " + theCategory.getId());
//        }
//    }

//    public void deleteCategory(Long theId) throws CustomException {
//        try{
//            categoryRepository.deleteById(theId);
//        } catch (Exception e){
//            throw new CustomException("존재하지 않는 카테고리입니다.", "카테고리 삭제 실패 : id = " + theId);
//        }
//    }
}
