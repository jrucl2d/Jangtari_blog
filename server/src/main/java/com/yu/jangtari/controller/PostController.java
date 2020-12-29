package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/category/{id}/posts")
    public ResponseEntity<CustomResponse> getPostList(@PathVariable(value = "id") Long categoryId){
        try{
            List<PostDTO.GetAll> results =  postService.getPostList(categoryId);
            return new ResponseEntity<>(new CustomResponse<>
                    (null, results),
                    HttpStatus.OK);
        } catch(CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
