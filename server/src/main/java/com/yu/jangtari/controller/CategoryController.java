package com.yu.jangtari.controller;

import com.yu.jangtari.common.ErrorEnum;
import com.yu.jangtari.common.ErrorType;
import com.yu.jangtari.common.ResponseType;
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
    public ResponseEntity<ResponseType> getAllCategory() {
        List<CategoryDTO.Get> response = categoryService.getAllCategories();
        return new ResponseEntity<>(new ResponseType<>
                (null, response),
                HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/addCategory")
    public ResponseEntity<ResponseType> addCategory(@RequestBody CategoryDTO.Add newCategory){

        int result = categoryService.addCategory(newCategory);
        if (result == 0){
            return new ResponseEntity<>(new ResponseType<>
                    (new ErrorType(ErrorEnum.INVALID_REQUEST_BODY,
                    "카테고리 양식 불충분"), null),
                    HttpStatus.BAD_REQUEST);
        }

        List<CategoryDTO.Get> response = categoryService.getAllCategories();
        return new ResponseEntity<>(new ResponseType<>
                (null, response),
                HttpStatus.OK);
    }
}
