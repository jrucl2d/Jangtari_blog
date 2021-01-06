package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CustomResponse> addCategory(@RequestBody CategoryDTO.Add newCategory){
        try{
            Long newId = categoryService.addCategory(newCategory);
            return new ResponseEntity<>(new CustomResponse<>(null, newId),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/category")
    public ResponseEntity<CustomResponse> updateCategory(@RequestBody CategoryDTO.Update theCategory){
        try {
            categoryService.updateCategory(theCategory);
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
