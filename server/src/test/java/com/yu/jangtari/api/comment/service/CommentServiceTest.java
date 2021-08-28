package com.yu.jangtari.api.comment.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CommentServiceTest extends ServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @BeforeAll
    static void beforeAll() {
        JwtInfo jwtInfo = JwtInfo.builder()
            .memberId(1L)
            .username("username")
            .nickName("nick")
            .roleType(RoleType.USER)
            .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtInfo, null, null));
    }

    @Test
    @DisplayName("정상적으로 comment 추가")
    void addComment()
    {
        // given
        CommentDto.Add dto = CommentDto.Add.builder()
            .postId(1L)
            .content("comment")
            .build();
        given(commentRepository.save(any())).willReturn(Comment.builder()
            .content("content")
            .member(Member.builder().id(1L).username("name").nickname("nick").build())
            .build());

        // when
        commentService.addComment(dto);

        // then
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("comment update가 성공적으로 진행 됨.")
    void updateComment()
    {
        // given
        CommentDto.Update dto = CommentDto.Update.builder()
            .content("newComment")
            .build();
        given(commentRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(
            Comment.builder()
                .content("content")
                .member(Member.builder()
                    .id(1L)
                    .username("user")
                    .nickname("nick")
                    .build())
                .build()
        ));

        // when
        CommentDto.Get updatedComment = commentService.updateComment(1L, dto);

        // then
        assertThat(updatedComment.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    @DisplayName("comment가 존재하지 않으면 COMMENT_NOT_FOUND 에러.")
    void updateComment_X()
    {
        // given
        CommentDto.Update dto = CommentDto.Update.builder()
            .content("newComment")
            .build();
        given(commentRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        // then
        BusinessException e = assertThrows(BusinessException.class, () -> commentService.updateComment(1L, dto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("childComment까지 모두 삭제됨")
    void deleteComment()
    {
        // given
        Comment comment = Comment.builder()
            .id(1L)
            .content("content")
            .childComments(Arrays.asList(
                Comment.builder().id(2L).content("child1").build()
                , Comment.builder().id(3L).content("child2").build()))
            .build();

        given(commentRepository.findById(anyLong())).willReturn(
            Optional.of(comment)
        );

        // when
        commentService.deleteComment(1L);

        // then
        assertThat(comment.getDeleteFlag().isDeleted()).isTrue();
        comment.getChildComments().forEach(
            child -> assertThat(child.getDeleteFlag().isDeleted()).isTrue()
        );
    }
}