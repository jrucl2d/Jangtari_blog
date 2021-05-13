package com.yu.jangtari.CommentTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.service.CommentService;
import com.yu.jangtari.service.MemberService;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

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
    void postId_를_사용해_해당_post_내의_comment들을_가져올_수_있음_O() {
        // given
        Post post = makePost();
        Member member = makeMember();
        Comment comment = makeComment(post, member);
        CommentDTO.Add commentDTO = CommentDTO.Add.builder().postId(1L).content("content").commenter("commenter").build();
        given(commentRepository.findCommentsOfPost(any())).willReturn(Arrays.asList(comment));
        // when
        commentService.getCommentsOfPost(1L);
        // then
        verify(commentRepository, times(1)).findCommentsOfPost(anyLong());
    }

    @Test
    void 저장되어_있는_post에_첫_댓글을_추가_O() {
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

    private Comment makeComment(Post post, Member member) {
        return Comment.builder().content("content").post(post).member(member).build();
    }
    private Post makePost() {
        return Post.builder().content("content").title("title").build();
    }
    private Member makeMember() {
        return Member.builder().username("username").nickname("nickname").password("pw").build();
    }
}
