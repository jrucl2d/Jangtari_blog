package com.yu.jangtari.api.comment.controller;

import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto.Get> getComments(@PathVariable("id") Long postId) {
        return commentService.getCommentsOfPost(postId);
    }

    @PostMapping("/user/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto.Get addComment(@Valid @RequestBody CommentDto.Add commentDto) {
        return commentService.addComment(commentDto);
    }

    @PutMapping("/user/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto.Get updateComment(@PathVariable("id") Long commentId, @Valid @RequestBody CommentDto.Update commentDto) {
        return commentService.updateComment(commentId, commentDto);
    }

    @DeleteMapping("/user/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
    }
}
