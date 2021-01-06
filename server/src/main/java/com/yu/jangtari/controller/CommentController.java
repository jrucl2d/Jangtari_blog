package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{id}/comments")
    public ResponseEntity<CustomResponse> getComments(@PathVariable(value = "id") Long postId){
        try{
            List<CommentDTO.Get> result =  commentService.getComments(postId);
            return new ResponseEntity<>(new CustomResponse(null, result),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/comment")
    public ResponseEntity<CustomResponse> addComment(@RequestBody CommentDTO.Add theComment, Principal principal){
        try{
            if(!theComment.getCommenter().equals(principal.getName())){
                throw new CustomException("사용자 정보가 올바르지 않습니다.", "댓글 추가 실패");
            }
            commentService.addComment(theComment);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/comment")
    public ResponseEntity<CustomResponse> updateComment(@RequestBody CommentDTO.Update theComment, Principal principal){
        try{
            if(!theComment.getCommenter().equals(principal.getName())){
                throw new CustomException("사용자 정보가 올바르지 않습니다.", "댓글 추가 실패");
            }
            commentService.updateComment(theComment);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.OK);
        } catch(CustomException e){
            return new ResponseEntity<>(new CustomResponse
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/comment/{id}")
    public ResponseEntity<CustomResponse> deleteComment(@PathVariable(value = "id")Long commentId){
        try{
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.CREATED);
        } catch (CustomException e){
            return new ResponseEntity<>(new CustomResponse<>
                    (new CustomError(e), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
