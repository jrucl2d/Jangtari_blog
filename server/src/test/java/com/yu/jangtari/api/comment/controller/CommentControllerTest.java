package com.yu.jangtari.api.comment.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.repository.MemberRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    private CommentRepository commentRepository;

    private static String accessToken;

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
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
    @DisplayName("parentComment 없는 comment를 정상적으로 추가할 수 있다.")
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
            .andExpect(jsonPath("$.content").value(dto.getContent()))
            .andExpect(jsonPath("$.username").value(member.getUsername()))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()))
            .andExpect(jsonPath("$.parentCommentId").doesNotExist())
            .andDo(print());
    }

    @Test
    @DisplayName("parentComment 있는 comment를 정상적으로 추가할 수 있다.")
    void addComment_1() throws Exception {
        Comment parent = commentRepository.save(Comment.builder()
            .member(member)
            .content("content1")
            .post(post)
            .build());

        CommentDto.Add dto = CommentDto.Add.builder()
            .postId(post.getId())
            .content("content2")
            .parentCommentId(parent.getId())
            .build();

        String content = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/user/comment")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(dto.getContent()))
            .andExpect(jsonPath("$.username").value(member.getUsername()))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()))
            .andExpect(jsonPath("$.parentCommentId").value(parent.getId()))
            .andDo(print());
    }

    @Test
    @DisplayName("postId로 댓글들을 정상적으로 가져올 수 있다.")
    void getComments() throws Exception
    {
        Comment parent = commentRepository.save(Comment.builder()
            .member(member)
            .content("content")
            .post(post)
            .build());
        Comment child1 = commentRepository.save(Comment.builder()
            .member(member)
            .content("content1")
            .parentComment(parent)
            .post(post)
            .build());
        Comment child2 = commentRepository.save(Comment.builder()
            .member(member)
            .content("content2")
            .parentComment(parent)
            .post(post)
            .build());

        mockMvc.perform(get("/post/" + post.getId() + "/comments")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$.[0].content").value(parent.getContent()))
            .andExpect(jsonPath("$.[0].parentCommentId").doesNotExist())
            .andExpect(jsonPath("$.[1].content").value(child1.getContent()))
            .andExpect(jsonPath("$.[1].parentCommentId").value(parent.getId()))
            .andExpect(jsonPath("$.[2].content").value(child2.getContent()))
            .andExpect(jsonPath("$.[2].parentCommentId").value(parent.getId()))
            .andDo(print());
    }

    @Test
    @DisplayName("댓글이 없다면 빈 값이 리턴된다.")
    void getComments1() throws Exception
    {
        mockMvc.perform(get("/post/" + post.getId() + "/comments")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andDo(print());
    }

    @Test
    @DisplayName("comment 정상적으로 수정할 수 있다.")
    void updateComment() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
            .member(member)
            .content("content")
            .post(post)
            .build());

        CommentDto.Update dto = CommentDto.Update.builder()
            .content("newContent")
            .build();

        String content = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/user/comment/" + comment.getId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(dto.getContent()))
            .andExpect(jsonPath("$.username").value(member.getUsername()))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()))
            .andExpect(jsonPath("$.parentCommentId").doesNotExist())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 comment 수정하려고 하면 에러 발생")
    void updateComment_X() throws Exception {
        CommentDto.Update dto = CommentDto.Update.builder()
            .content("newContent")
            .build();

        String content = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/user/comment/10000")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value(ErrorCode.COMMENT_NOT_FOUND_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.COMMENT_NOT_FOUND_ERROR.getMessage()))
            .andDo(print());
    }

    @Test
    @DisplayName("comment 삭제시 childComment 까지 정상적으로 삭제할 수 있다.")
    void deleteComment() throws Exception {
        Comment parent = commentRepository.save(Comment.builder()
            .member(member)
            .content("content")
            .post(post)
            .build());
        Comment child1 = commentRepository.save(Comment.builder()
            .member(member)
            .content("content1")
            .parentComment(parent)
            .post(post)
            .build());
        Comment child2 = commentRepository.save(Comment.builder()
            .member(member)
            .content("content2")
            .parentComment(parent)
            .post(post)
            .build());

        parent.getChildComments().addAll(Arrays.asList(child1, child2));

        mockMvc.perform(delete("/user/comment/" + parent.getId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        commentRepository.findAll().forEach(
            comment -> assertThat(comment.getDeleteFlag().isDeleted()).isTrue()
        );
    }
}