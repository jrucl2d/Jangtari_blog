package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.vo.PageVO;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/category/{id}/posts")
    public ResponseEntity<CustomResponse> getPostList(@PathVariable(value = "id") Long categoryId, PageVO pageVO){
        try{
            Page<PostDTO.GetAll> results =  postService.getPostList(categoryId ,pageVO);
            return new ResponseEntity<>(new CustomResponse<>
                    (null, results),
                    HttpStatus.OK);
        } catch(CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<CustomResponse> getPost(@PathVariable(value = "id") Long postId){
        try{
            PostDTO.GetOne result = postService.getPost(postId);
            return new ResponseEntity<>(new CustomResponse<>
                    (null, result),
                    HttpStatus.OK);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addPost")
    public ResponseEntity<CustomResponse> addPost(@RequestBody PostDTO.Add thePost){
        try{
            postService.addPost(thePost);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatePost")
    public ResponseEntity<CustomResponse> updatePost(@RequestBody PostDTO.Update thePost){
        try{
            postService.updatePost(thePost);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<CustomResponse> deletePost(@PathVariable(value = "id")Long postId){
        try{
            postService.deletePost(postId);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
