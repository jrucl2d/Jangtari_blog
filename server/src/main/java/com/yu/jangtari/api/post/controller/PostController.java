package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto.GetOne getPost(@PathVariable(value = "id") Long postId){
        return postService.getPost(postId);
    }

    @GetMapping("/category/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto.ListGetElement> getPostList(@PathVariable(value = "id") Long categoryId, PageRequest pageRequest) {
        return postService.getPostList(categoryId, pageRequest);
    }

    @PostMapping(value = "/admin/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PostDto.ListGetElement addPost(
        @Valid PostDto.Add postDto
        , @RequestParam(required = false, name = "pictures") List<MultipartFile> pictures)
    {
        return postService.addPost(postDto, pictures);
    }

    @PutMapping(value = "/admin/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PostDto.GetOne updatePost(
        @Valid PostDto.Update postDto
        , @RequestParam(required = false, name = "pictures") List<MultipartFile> pictures)
    {
        return postService.updatePost(postDto, pictures);
    }

    @DeleteMapping("/admin/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return "OK";
    }
}
