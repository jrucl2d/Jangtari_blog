package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.service.CategoryService;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

class PostControllerTest extends IntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${jangtari.name}")
    private String jangtariName;

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

        category = categoryService.addCategory(CategoryDto.Add.builder()
            .name("category")
            .build(), null);
    }

    @Test
    @DisplayName("정상적으로 post 추가")
    void addPost() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("name", "category1");
//
//        mockMvc.perform(post("/admin/category")
//                .header(HttpHeaders.AUTHORIZATION, accessToken)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .params(params))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.name").value("category1"))
//            .andDo(print());
    }
}