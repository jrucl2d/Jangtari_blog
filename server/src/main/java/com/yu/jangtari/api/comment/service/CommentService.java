package com.yu.jangtari.api.comment.service;

import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.repository.CommentRepository;
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

    @Transactional(readOnly = true)
    public List<Comment> getCommentsOfPost(Long postId) {
        return commentRepository.findCommentsOfPost(postId);
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_ERROR));
    }

    public CommentDto.Get addComment(CommentDto.Add commentDTO) {
        Comment comment = commentRepository.save(commentDTO.toEntity());
        return CommentDto.Get.of(comment);
    }

    public CommentDto.Get updateComment(Long commentId, CommentDto.Update commentDTO) {
        Comment comment = getCommentByIdAndMemberId(commentId);
        return CommentDto.Get.of(comment.updateComment(commentDTO));
    }

    private Comment getCommentByIdAndMemberId(Long commentId) {
        Long memberId = AuthUtil.getMemberId();
        return commentRepository.findByIdAndMemberId(commentId, memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_ERROR));
    }

    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.getChildComments().forEach(Comment::softDelete);
        comment.softDelete();
    }
}
