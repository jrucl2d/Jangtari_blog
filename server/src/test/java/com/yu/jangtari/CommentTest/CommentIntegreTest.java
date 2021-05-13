package com.yu.jangtari.CommentTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.service.CategoryService;
import com.yu.jangtari.service.CommentService;
import com.yu.jangtari.service.MemberService;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentIntegreTest extends IntegrationTest {

    @Autowired
    CommentService commentService;
    @Autowired
    PostService postService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("addComment, getCommentsOfPost 성공 - 첫 댓글")
    void addComment_getCommentsOfPost_O() {
        // given
        Category category = makeCategory();
        memberRegister();
        makePostInCategory(category);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).content("content").commenter("username").build();
        Comment comment = commentService.addComment(commentDTO);
        // when
        List<Comment> comments = commentService.getCommentsOfPost(1L);
        // then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }
    @Test
    @DisplayName("addComment, getCommentsOfPost 성공 - 대댓글 확인")
    void addComment_getCommentsOfPost_O2() {
        // given
        Category category = makeCategory();
        memberRegister();
        makePostInCategory(category);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).content("content").commenter("username").build();
        Comment comment = commentService.addComment(commentDTO);
        CommentDTO.Add commentDTO2 = CommentDTO.Add.builder().postId(1L).content("content2").commenter("username").parentCommentId(1L).build();
        Comment comment1 = commentService.addComment(commentDTO2);
        // when
        List<Comment> comments = commentService.getCommentsOfPost(1L);
        // then
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0)).isEqualTo(comment);
        assertThat(comments.get(1)).isEqualTo(comment1);
        assertThat(comments.get(1).getParentComment()).isEqualTo(comments.get(0));
        assertThat(comments.get(0).getChildComments().get(0)).isEqualTo(comments.get(1));
    }
    @Test
    @DisplayName("addComment, getCommentsOfPost 성공 - 아무 댓글 없는 것 확인")
    void addComment_getCommentsOfPost_O3() {
        // given
        Category category = makeCategory();
        memberRegister();
        makePostInCategory(category);
        // when
        List<Comment> comments = commentService.getCommentsOfPost(1L);
        // then
        assertThat(comments.size()).isEqualTo(0);
    }

    private Post makePostInCategory(Category category) {
        PostDTO.Add postDTO = PostDTO.Add.builder().content("content").categoryId(category.getId()).title("title").template(1).build();
        return postService.addPost(postDTO);
    }
    private Category makeCategory() {
        CategoryDTO.Add categoryDTO = CategoryDTO.Add.builder().name("category").build();
        return categoryService.addCategory(categoryDTO);
    }
    private Member memberRegister() {
        MemberDTO.Add memberDTO = MemberDTO.Add.builder().username("username").nickname("nickname").password("password").build();
        return memberService.addMember(memberDTO);
    }
}
