package com.yu.jangtari.CommentTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("addPost 테스트")
    class AddPost {
        @Test
        @DisplayName("addComment O, 부모 comment 없는 comment 추가 성공")
        void addComment_O() throws Exception {
            Member member = Member.builder().username("user").nickname("nick").password("pass").build();
            memberRepository.save(member);
            Category category = Category.builder().name("category").build();
            category = categoryRepository.save(category);
            Post post = Post.builder().title("title").content("content").template(1).category(category).build();
            postRepository.save(post);

            CommentDTO.Add commentDTO = CommentDTO.Add.builder().commenter("user").content("comment").postId(1L).build();
            String content = objectMapper.writeValueAsString(commentDTO);

            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("comment"))
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.nickname").value("nick"))
                    .andExpect(jsonPath("$.parentCommentId").doesNotExist())
                    .andDo(print());
        }
        @Test
        @DisplayName("addComment O, 부모 comment 있는 comment 추가 성공")
        void addComment_O1() throws Exception {
            Member member = Member.builder().username("user").nickname("nick").password("pass").build();
            memberRepository.save(member);
            Category category = Category.builder().name("category").build();
            category = categoryRepository.save(category);
            Post post = Post.builder().title("title").content("content").template(1).category(category).build();
            postRepository.save(post);

            CommentDTO.Add commentDTO = CommentDTO.Add.builder().commenter("user").content("comment").postId(1L).build();
            String content = objectMapper.writeValueAsString(commentDTO);

            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated());

            commentDTO = CommentDTO.Add.builder().commenter("user").content("comment2").postId(1L).parentCommentId(1L).build();
            content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("comment2"))
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.nickname").value("nick"))
                    .andExpect(jsonPath("$.parentCommentId").value(1L))
                    .andDo(print());
        }
        @Test
        @DisplayName("addComment X, 파라미터 맞지 않는 DTO 사용시 Invalid Input Error 발생")
        void addComment_X() throws Exception {
            Member member = Member.builder().username("user").nickname("nick").password("pass").build();
            memberRepository.save(member);
            Category category = Category.builder().name("category").build();
            category = categoryRepository.save(category);
            Post post = Post.builder().title("title").content("content").template(1).category(category).build();
            postRepository.save(post);

            // postId 없이 전송
            CommentDTO.Add commentDTO = CommentDTO.Add.builder().commenter("user").content("comment").build();
            String content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu001"))
                    .andDo(print());

            // commenter 없이 전송
            commentDTO = CommentDTO.Add.builder().postId(1L).content("comment").build();
            content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu001"))
                    .andDo(print());
            // commenter 없이 전송
            commentDTO = CommentDTO.Add.builder().postId(1L).content("comment").build();
            content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu001"))
                    .andDo(print());
        }
        @Test
        @DisplayName("addComment X, 존재하지 않는 commenter로 보내면 No Such Member Exception")
        void addComment_X1() throws Exception {
            Category category = Category.builder().name("category").build();
            category = categoryRepository.save(category);
            Post post = Post.builder().title("title").content("content").template(1).category(category).build();
            postRepository.save(post);

            CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).commenter("no").content("comment").build();
            String content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("No Such Member"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu005"))
                    .andDo(print());
        }
        @Test
        @DisplayName("addComment X, 존재하지 않는 postId로 보내면 No Such Post Exception")
        void addComment_X2() throws Exception {
            CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(2L).commenter("user").content("comment").build();
            String content = objectMapper.writeValueAsString(commentDTO);
            mockMvc.perform(post("/user/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("No Such Post"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu010"))
                    .andDo(print());
        }
    }
    @Test
    @DisplayName("getComments O, 전체 comment 불러오기 성공")
    void getComments_O() throws Exception {
        addBeforeTask();
        mockMvc.perform(get("/post/1/comments"))
                .andExpect(jsonPath("$").value(Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].content").value("comment"))
                .andExpect(jsonPath("$.[1].content").value("comment1"))
                .andDo(print());
    }
    @Test
    @DisplayName("getComments O, 삭제된 comment는 안 불러오기 성공")
    void getComments_O1() throws Exception {
        addBeforeTask();
        Comment comment = commentRepository.findById(1L).get();
        comment.getDeleteFlag().softDelete();
        commentRepository.save(comment);

        mockMvc.perform(get("/post/1/comments"))
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].content").value("comment1"))
                .andDo(print());
    }
    @Test
    @DisplayName("getComments O, 댓글이 없으면 안 불러오기 성공")
    void getComments_O2() throws Exception {
        mockMvc.perform(get("/post/1/comments"))
                .andExpect(jsonPath("$").value(Matchers.hasSize(0)))
                .andDo(print());
    }
    @Test
    @DisplayName("updateComment O, 댓글 수정 성공")
    void updateComment_O() throws Exception {
        addBeforeTask();
        CommentDTO.Update commentDTO = CommentDTO.Update.builder().content("updated").commenter("user").build();
        String content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(put("/user/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$.content").value("updated"))
                .andDo(print());
    }
    @Test
    @DisplayName("updateComment X, 파라미터 맞지 않는 DTO 사용시 Invalid Input Error 발생")
    void updateComment_X() throws Exception {
        addBeforeTask();
        // content 비어있는 경우
        CommentDTO.Update commentDTO = CommentDTO.Update.builder().commenter("user").build();
        String content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(put("/user/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu001"))
                .andDo(print());
        // commenter 비어있는 경우
        commentDTO = CommentDTO.Update.builder().content("updated").build();
        content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(put("/user/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu001"))
                .andDo(print());
    }
    @Test
    @DisplayName("updateComment X, 존재하지 않는 commenter로 보내면 No Such Member Exception")
    void updateComment_X1() throws Exception {
        addBeforeTask();
        CommentDTO.Update commentDTO = CommentDTO.Update.builder().content("updated").commenter("no").build();
        String content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(put("/user/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No Such Member"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu005"))
                .andDo(print());
    }
    @Test
    @DisplayName("deleteComment O, 댓글 삭제 성공")
    void deleteComment_O() throws Exception {
        addBeforeTask();

        mockMvc.perform(delete("/user/comment/1"))
                .andExpect(jsonPath("$").value("OK"))
                .andDo(print());
    }
    @Test
    @DisplayName("deleteComment O, 부모 댓글 삭제시 자식 댓글도 삭제")
    void deleteComment_O1() throws Exception {
        addBeforeTask();

        CommentDTO.Add commentDTO = CommentDTO.Add.builder().commenter("user").content("comment2").postId(1L).parentCommentId(1L).build();
        String content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        mockMvc.perform(delete("/user/comment/1"))
                .andExpect(jsonPath("$").value("OK"))
                .andDo(print());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.get(0).getChildComments().get(0)).isEqualTo(comments.get(2));
        assertThat(comments.get(0).getDeleteFlag().isDeleteFlag()).isTrue();
        assertThat(comments.get(1).getDeleteFlag().isDeleteFlag()).isFalse();
        assertThat(comments.get(2).getDeleteFlag().isDeleteFlag()).isTrue();
    }
    
    private void addBeforeTask() throws Exception {
        Member member = Member.builder().username("user").nickname("nick").password("pass").build();
        memberRepository.save(member);
        Category category = Category.builder().name("category").build();
        category = categoryRepository.save(category);
        Post post = Post.builder().title("title").content("content").template(1).category(category).build();
        postRepository.save(post);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().commenter("user").content("comment").postId(1L).build();
        String content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());
        commentDTO = CommentDTO.Add.builder().commenter("user").content("comment1").postId(1L).build();
        content = objectMapper.writeValueAsString(commentDTO);
        mockMvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());
    }
}
