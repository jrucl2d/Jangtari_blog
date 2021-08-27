package com.yu.jangtari.api.comment.service;

import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<Comment> getCommentsOfPost(Long postId) {
        return commentRepository.findCommentsOfPost(postId);
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_ERROR));
    }

    public Comment addComment(CommentDto.Add commentDTO) {
        return commentRepository.save(commentDTO.toEntity());
    }

    // TODO : commentId와 memberId로 가져오는 방식으로 레포지토리 메소드 추가
    public Comment updateComment(Long commentId, CommentDto.Update commentDTO) {
        Long memberId = AuthUtil.getMemberId();
        Comment comment = getComment(commentId);
        comment.updateComment(commentDTO);
        return comment;
    }

    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.getChildComments().forEach(Comment::softDelete);
        comment.softDelete();
    }
}
