package com.yu.jangtari.CommentTest;

import com.yu.jangtari.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentDomainTest {

    @Test
    @DisplayName("단순 댓글 생성 성공")
    void createComment_O() {
        Comment comment = Comment
                .builder()
                .content("content")
                .post(Post.builder().build())
                .member(Member.builder().build())
                .build();
        assertThat(comment).isNotNull();
    }
    @Test
    @DisplayName("대댓글 생성 성공")
    void createChildComment_O() {
        Comment comment = Comment
                .builder()
                .content("content")
                .post(Post.builder().build())
                .member(Member.builder().build())
                .build();
        Comment childComment = Comment
                .builder()
                .content("child_content")
                .post(Post.builder().build())
                .member(Member.builder().build())
                .build();
        comment.addChildComment(childComment);
        assertThat(comment.getChildComments().size()).isEqualTo(1);
        assertThat(childComment.getParentComment()).isNotNull();
    }
    @Test
    @DisplayName("Commentdp Post와 Member 추가 성공")
    void initPostAndMember_O() {
        Post post = Post.builder().build();
        Member member = Member.builder().build();
        Comment comment = Comment.builder().build();
        comment.initPostAndMember(post, member);
        assertThat(comment.getMember()).isNotNull();
        assertThat(comment.getPost()).isNotNull();
        assertThat(comment.getPost().getComments().get(0)).isEqualTo(comment);
    }
}
