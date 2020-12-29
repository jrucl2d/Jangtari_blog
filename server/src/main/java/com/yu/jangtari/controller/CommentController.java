package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<CustomResponse> addComment(@RequestBody CommentDTO.Add theComment){
        try{
            commentService.addComment(theComment);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateComment")
    public ResponseEntity<CustomResponse> updateComment(@RequestBody CommentDTO.Update theComment){
        try{
            commentService.updateComment(theComment);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.OK);
        } catch(CustomException e){
            return new ResponseEntity<>(new CustomResponse
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
