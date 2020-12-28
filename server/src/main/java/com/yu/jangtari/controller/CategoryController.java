package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.ErrorEnum;
import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Transactional(readOnly = true)
    @GetMapping("/getAllCategories")
    public ResponseEntity<CustomResponse> getAllCategory() {
        List<CategoryDTO.Get> response = categoryService.getAllCategories();
        return new ResponseEntity<>(new CustomResponse<>
                (null, response),
                HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/addCategory")
    public ResponseEntity<CustomResponse> addCategory(@RequestBody CategoryDTO.Add newCategory){
        try{
            categoryService.addCategory(newCategory);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PutMapping("/updateCategory")
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

    @DeleteMapping("/deleteCategory/{id}")
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
