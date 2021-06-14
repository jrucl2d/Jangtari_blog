package com.yu.jangtari.controller;

import com.yu.jangtari.common.ErrorResponse;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글을 추가할 경우 댓글만 다시 불러오기 위한 컨트롤러
    @GetMapping("/post/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO.Get> getComments(@PathVariable("id") final Long postId) {
        return commentService.getCommentsOfPost(postId).stream().map(CommentDTO.Get::of).collect(Collectors.toList());
    }

//    @PostMapping("/user/comment")
//    public ResponseEntity<CustomResponse> addComment(@RequestBody CommentDTO.Add theComment, Principal principal){
//        try{
//            if(!theComment.getCommenter().equals(principal.getName())){
//                throw new CustomException("사용자 정보가 올바르지 않습니다.", "댓글 추가 실패");
//            }
//            commentService.addComment(theComment);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping("/user/comment")
//    public ResponseEntity<CustomResponse> updateComment(@RequestBody CommentDTO.Update theComment, Principal principal){
//        try{
//            if(!theComment.getCommenter().equals(principal.getName())){
//                throw new CustomException("사용자 정보가 올바르지 않습니다.", "댓글 추가 실패");
//            }
//            commentService.updateComment(theComment);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.OK);
//        } catch(CustomException e){
//            return new ResponseEntity<>(new CustomResponse
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/user/comment/{id}")
//    public ResponseEntity<CustomResponse> deleteComment(@PathVariable(value = "id")Long commentId){
//        try{
//            commentService.deleteComment(commentId);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
}
