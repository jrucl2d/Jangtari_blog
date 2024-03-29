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
    public List<CommentDto.Get> getCommentsOfPost(Long postId) {
        return CommentDto.Get.toList(commentRepository.findCommentsOfPost(postId));
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_ERROR));
    }

    public CommentDto.Get addComment(CommentDto.Add commentDTO) {
        Comment comment = commentDTO.toEntity();
        if (commentDTO.getParentCommentId() != null) {
            getComment(commentDTO.getParentCommentId()).getChildComments().add(comment);
        }
        comment = commentRepository.save(comment);
        return CommentDto.Get.of(comment);
    }

    public CommentDto.Get updateComment(Long commentId, CommentDto.Update commentDTO) {
        Comment comment = getCommentByIdAndUsername(commentId);
        return CommentDto.Get.of(comment.updateComment(commentDTO));
    }

    private Comment getCommentByIdAndUsername(Long commentId) {
        String username = AuthUtil.getUsername();
        return commentRepository.findByIdAndUsername(commentId, username)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_ERROR));
    }

    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.getChildComments().forEach(Comment::softDelete);
        comment.softDelete();
    }
}
