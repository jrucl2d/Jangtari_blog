package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@Sql("classpath:postControllerTest.sql")
class PostControllerTest extends IntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    private static String accessToken;
    private Category category;
    private List<Post> posts;
    private Member commenter;
    private Member jangBoy;

    @BeforeEach
    void setUp() {
    }

    @Test
    void name()
    {
        // given
        System.out.println(memberRepository.getOne(1L));
        // when

        // then
    }
}