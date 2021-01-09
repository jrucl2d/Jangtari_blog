package com.yu.jangtari.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CustomResponse> getAllCategory() {
        List<CategoryDTO.Get> response = categoryService.getAllCategories();
        return new ResponseEntity<>(new CustomResponse<>
                (null, response),
                HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CustomResponse> addCategory(@RequestPart("category") String newCategory,
                                                      @RequestPart("image") MultipartFile categoryImage) throws GeneralSecurityException, IOException {
        try{
            Long newId = categoryService.addCategory(newCategory, categoryImage);
            return new ResponseEntity<>(new CustomResponse<>(null, newId),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/category/nimg")
    public ResponseEntity<CustomResponse> addCategory2(@RequestPart("category") String newCategory) throws GeneralSecurityException, IOException {
        try{
            Long newId = categoryService.addCategory(newCategory, null);
            return new ResponseEntity<>(new CustomResponse<>(null, newId),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/category")
    public ResponseEntity<CustomResponse> updateCategory(@RequestPart("category") String categoryString,
                                                         @RequestPart("image")MultipartFile categoryImage) throws GeneralSecurityException, IOException{
        try {
            CategoryDTO.Update theCategory = new ObjectMapper().readValue(categoryString, CategoryDTO.Update.class);
            categoryService.updateCategory(theCategory, categoryImage);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.ACCEPTED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/admin/category/nimg")
    public ResponseEntity<CustomResponse> updateCategory2(@RequestPart("category") String categoryString) throws GeneralSecurityException, IOException{
        try {
            CategoryDTO.Update theCategory = new ObjectMapper().readValue(categoryString, CategoryDTO.Update.class);
            categoryService.updateCategory(theCategory, null);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.ACCEPTED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<CustomResponse> deleteCategory(@PathVariable(value = "id") Long theId){
        try {
            categoryService.deleteCategory(theId);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.ACCEPTED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}