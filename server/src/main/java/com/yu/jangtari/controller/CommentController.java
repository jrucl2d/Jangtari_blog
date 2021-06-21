package com.yu.jangtari.controller;

import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO.Get> getComments(@PathVariable("id") final Long postId) {
        return commentService.getCommentsOfPost(postId).stream().map(CommentDTO.Get::of).collect(Collectors.toList());
    }
    @PostMapping("/user/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO.Get addComment(@RequestBody final CommentDTO.Add commentDTO) {
        return CommentDTO.Get.of(commentService.addComment(commentDTO));
    }
    @PutMapping("/user/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO.Get updateComment(@PathVariable("id") final Long commentId, @RequestBody final CommentDTO.Update commentDTO) {
        return CommentDTO.Get.of(commentService.updateComment(commentId, commentDTO));
    }
    @DeleteMapping("/user/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@PathVariable("id") final Long commentId) {
        commentService.deleteComment(commentId);
        return "OK";
    }
}
