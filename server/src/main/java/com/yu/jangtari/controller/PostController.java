package com.yu.jangtari.controller;

import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.service.PostService;
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
    public PostDTO.GetOne getPost(@PathVariable(value = "id") Long postId){
        return PostDTO.GetOne.of(postService.getOne(postId));
    }
    @GetMapping("/category/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDTO.Get> getPostList(@PathVariable(value = "id") Long categoryId, PageRequest pageRequest) {
        return postService.getPostList(categoryId, pageRequest);
    }
    @PostMapping(value = "/admin/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO.Get addPost(@Valid final PostDTO.Add postDTO) {
        return PostDTO.Get.of(postService.addPost(postDTO));
    }
    @PostMapping(value = "/admin/post/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PostDTO.Get updatePost(@PathVariable("id") final Long postId, @Valid final PostDTO.Update postDTO) {
        return PostDTO.Get.of(postService.updatePost(postId, postDTO));
    }
    @DeleteMapping("/admin/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@PathVariable("id") final Long postId) {
        postService.deletePost(postId);
        return "OK";
    }
}
