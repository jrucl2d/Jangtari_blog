package com.yu.jangtari.api.comment.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.repository.post.PostRepository;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends IntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    private static String accessToken;

    private Post post;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(Member.builder()
            .username("user")
            .nickname("nick")
            .password(passwordEncoder.encode("password"))
            .build());
        accessToken = JwtUtil.createAccessToken(
            JwtInfo.builder()
                .memberId(member.getId())
                .username("user")
                .nickName("nick")
                .roleType(RoleType.USER)
                .build());
        Category category = categoryRepository.save(
            Category.builder()
                .name("name")
                .build()
        );
        post = postRepository.save(Post.builder()
            .content("content")
            .template(1)
            .title("title")
            .category(category)
            .build());
    }

    @Test
    @DisplayName("comment를 정상적으로 추가할 수 있다.")
    void addComment() throws Exception {
        CommentDto.Add dto = CommentDto.Add.builder()
            .postId(post.getId())
            .content("content")
            .build();

        String content = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/user/comment")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.name").value("category1"))
            .andDo(print());
    }
}