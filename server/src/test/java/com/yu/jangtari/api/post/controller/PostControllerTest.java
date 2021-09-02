package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.service.CategoryService;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.repository.post.PostRepository;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends IntegrationTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static String accessToken;
    private CategoryDto.Get category;
    private List<Post> posts;
    private Member commenter;
    private Member jangBoy;

    @BeforeEach
    void setUp() {
        memberService.join(MemberDto.Add.builder()
            .username("jangtari")
            .nickname("nickname")
            .password("password")
            .build());
        jangBoy = memberService.getOne("jangtari");
        accessToken = JwtUtil.createAccessToken(JwtInfo.builder()
            .username("jangtari")
            .nickName("nickname")
            .roleType(RoleType.ADMIN)
            .build());

        category = categoryService.addCategory(CategoryDto.Add.builder()
            .name("category")
            .build(), null);
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
}