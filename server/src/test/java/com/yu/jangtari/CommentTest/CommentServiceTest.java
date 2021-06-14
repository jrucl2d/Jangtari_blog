package com.yu.jangtari.CommentTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.exception.NoMasterException;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.service.CommentService;
import com.yu.jangtari.service.MemberService;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommentServiceTest extends ServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @Mock
    private MemberService memberService;

    @Test
    @DisplayName("postId를 사용해 해당 post 내의 comment들을 가져올 수 있음")
    void getCommentsOfPost_O() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).content("content").commenter("commenter").build();
        given(commentRepository.findCommentsOfPost(any())).willReturn(Collections.singletonList(comment));
        // when
        commentService.getCommentsOfPost(1L);
        // then
        verify(commentRepository, times(1)).findCommentsOfPost(anyLong());
    }

    @Test
    @DisplayName("저장되어 있는 post에 첫 댓글을 추가")
    void addComment_O() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).content("content").commenter("commenter").build();
        given(postService.findOne(anyLong())).willReturn(post);
        given(memberService.getMemberByName(anyString())).willReturn(member);
        given(commentRepository.save(any())).willReturn(comment);
        // when
        commentService.addComment(commentDTO);
        // then
        verify(postService, times(1)).findOne(anyLong());
        verify(memberService, times(1)).getMemberByName(anyString());
    }
    @Test
    @DisplayName("updateComment로 댓글 수정 성공")
    void updateComment_O() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(memberService.getMemberByName(anyString())).willReturn(member);
        CommentDTO.Update commentDTO = CommentDTO.Update.builder().content("newComment").commenter("username").build();
        // when
        Comment newComment = commentService.updateComment(1L, commentDTO);
        // then
        assertThat(newComment.getContent()).isEqualTo(commentDTO.getContent());
    }

    @Test
    @DisplayName("작성자가 아니면 updateComment 실행시 NoMasterException")
    void updateComment_X() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(memberService.getMemberByName(anyString())).willReturn(Member.builder().username("no").build());
        CommentDTO.Update commentDTO = CommentDTO.Update.builder().content("newComment").commenter("username").build();
        // when, then
        assertThrows(NoMasterException.class, () -> commentService.updateComment(1L, commentDTO));
    }

    @Test
    @DisplayName("부모 코멘트와 자식 코멘트 중 자식 코멘트만 삭제하면 부모 코멘트는 남아있음")
    void deleteComment_O() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        Comment childComment = Comment.builder().content("child").build();
        comment.initPostAndMember(post, member);
        comment.addChildComment(childComment);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(childComment));
        // when
        commentService.deleteComment(1L);
        // then
        assertThat(comment.getChildComments().get(0)).isEqualTo(childComment);
        assertThat(childComment.getParentComment()).isEqualTo(comment);
        assertThat(comment.getDeleteFlag().isDeleteFlag()).isFalse();
        assertThat(comment.getChildComments().get(0).getDeleteFlag().isDeleteFlag()).isTrue();
    }
    @Test
    @DisplayName("부모 코멘트와 자식 코멘트 중 부모 코멘트 삭제시 자식까지 삭제")
    void deleteComment_O2() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        Comment childComment = Comment.builder().content("child").build();
        comment.initPostAndMember(post, member);
        comment.addChildComment(childComment);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        // when
        commentService.deleteComment(1L);
        // then
        assertThat(comment.getDeleteFlag().isDeleteFlag()).isTrue();
        assertThat(comment.getChildComments().get(0).getDeleteFlag().isDeleteFlag()).isTrue();
    }

    private Comment makeComment(Post post, Member member) {
        Comment comment = Comment.builder().content("content").build();
        comment.initPostAndMember(post, member);
        return comment;
    }
    private Post makePost() {
        return Post.builder().content("content").title("title").build();
    }
    private Member makeMember() {
        return Member.builder().username("username").nickname("nickname").password("pw").build();
    }
}
