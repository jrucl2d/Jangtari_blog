package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.service.CategoryService;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.service.CommentService;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends IntegrationTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    private static String accessToken;
    private CategoryDto.Get category;
    private List<PostDto.ListGetElement> posts;
    private Member jangBoy;
    private CommentDto.Get parentComment;
    private CommentDto.Get childComment;

    @BeforeEach
    void setUp() {
        memberService.join(MemberDto.Add.builder()
            .username("jangtari")
            .nickname("nickname")
            .password("password")
            .build());
        jangBoy = memberService.getOne("jangtari");
        JwtInfo jwtInfo = JwtInfo.builder()
            .username("jangtari")
            .nickName("nickname")
            .roleType(RoleType.ADMIN)
            .build();
        accessToken = JwtUtil.createAccessToken(jwtInfo);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwtInfo, null, null)
        );

        category = categoryService.addCategory(CategoryDto.Add.builder()
            .name("category")
            .build(), null);

        posts = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i ->
            posts.add(
                postService.addPost(
                    PostDto.Add.builder()
                        .title("title" + i)
                        .categoryId(category.getCategoryId())
                        .template(i % 2)
                        .content("content" + i)
                        .hashtags(
                            Arrays.asList("hashtag1", "hashtag2")
                        )
                        .build()
                    , null
                )
            )
        );

        parentComment = commentService.addComment(CommentDto.Add.builder()
            .postId(posts.get(0).getPostId())
            .content("comment1")
            .build());

        childComment = commentService.addComment(CommentDto.Add.builder()
            .postId(posts.get(0).getPostId())
            .content("comment1")
            .parentCommentId(parentComment.getCommentId())
            .build());
    }

    @Test
    @DisplayName("정상적으로 post 추가")
    void addPost() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", String.valueOf(category.getCategoryId()));
        params.add("title", "postTitle");
        params.add("content", "postContent");

        mockMvc.perform(post("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("postTitle"))
            .andDo(print());
    }

    @Test
    @DisplayName("categoryId에 맞는 category 없다면 CATEGORY_NOT_FOUND_ERROR 발생")
    void addPost_X() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", "123123123123123");
        params.add("title", "postTitle");
        params.add("content", "postContent");

        mockMvc.perform(post("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getMessage()))
            .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 post 수정")
    void updatePost() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("postId", String.valueOf(posts.get(0).getPostId()));
        params.add("title", "newTitle");
        params.add("content", "newContent");
        params.add("template", "0");
        List<String> hashtags = Arrays.asList("hashtag2", "hashtag3");
        String hashtagsString = objectMapper.writeValueAsString(hashtags);
        params.add("hashtags", hashtagsString);

        mockMvc.perform(put("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("newTitle"))
            .andExpect(jsonPath("$.content").value("newContent"))
            .andDo(print());
    }

    // TODO : getJoining에서 제대로 된 join이 안 됨. 처리 필요
}