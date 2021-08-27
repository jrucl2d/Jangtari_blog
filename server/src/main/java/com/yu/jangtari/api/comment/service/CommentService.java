package com.yu.jangtari.api.comment.service;

import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.repository.CommentRepository;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.common.exception.NoMasterException;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
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
        Comment comment = commentDTO.toEntity();
        Post post = postService.getOne(commentDTO.getPostId());
        Member member = memberService.getMemberByName(commentDTO.getCommenter());
        comment.initPostAndMember(post, member);
        addParentIfExists(comment, commentDTO.getParentCommentId());
        return commentRepository.save(comment);
    }
    private void addParentIfExists(Comment comment, Long parentId) {
        if (parentId == null) return;
        Comment parentComment = getComment(parentId);
        parentComment.addChildComment(comment);
    }

    public Comment updateComment(Long commentId, CommentDto.Update commentDTO) {
        Comment comment = getComment(commentId);
        verifyCommenter(commentDTO.getCommenter(), comment);
        comment.updateComment(commentDTO);
        return comment;
    }

    // TODO : AuthUtil 생성한 뒤 여기 삭제
    private void verifyCommenter(String username, Comment comment) {
        Member member = memberService.getMemberByName(username);
        if (!comment.getMember().equals(member)) {
            throw new NoMasterException();
        }
    }

    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.getChildComments().forEach(Comment::softDelete);
        comment.softDelete();
    }
}
