package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto.GetOne getPost(@PathVariable(value = "id") Long postId){
        return PostDto.GetOne.of(postService.getOneJoining(postId));
    }
    @GetMapping("/category/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto.ListGetElement> getPostList(@PathVariable(value = "id") Long categoryId, PageRequest pageRequest) {
        return postService.getPostList(categoryId, pageRequest);
    }
    @PostMapping(value = "/admin/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto.ListGetElement addPost(@Valid final PostDto.Add postDTO) {
        return PostDto.ListGetElement.of(postService.addPost(postDTO));
    }
    @PostMapping(value = "/admin/post/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PostDto.GetOne updatePost(@PathVariable("id") final Long postId, @Valid final PostDto.Update postDTO) {
        return PostDto.GetOne.of(postService.updatePost(postId, postDTO));
    }
    @DeleteMapping("/admin/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@PathVariable("id") final Long postId) {
        postService.deletePost(postId);
        return "OK";
    }
}
