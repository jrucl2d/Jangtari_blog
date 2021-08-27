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
        final Comment comment = commentDTO.toEntity();
        final Post post = postService.getOne(commentDTO.getPostId());
        final Member member = memberService.getMemberByName(commentDTO.getCommenter());
        comment.initPostAndMember(post, member);
        addParentIfExists(comment, commentDTO.getParentCommentId());
        return commentRepository.save(comment);
    }
    private void addParentIfExists(Comment comment, Long parentId) {
        if (parentId == null) return;
        final Comment parentComment = getComment(parentId);
        parentComment.addChildComment(comment);
    }

    public Comment updateComment(Long commentId, CommentDto.Update commentDTO) {
        Comment comment = getComment(commentId);
        verifyCommenter(commentDTO.getCommenter(), comment);
        comment.updateComment(commentDTO);
        return comment;
    }

    // Member 엔티티에 equals, hashCode가 id와 username을 기반으로 구현되어 있다.
    private void verifyCommenter(final String username, final Comment comment) {
        Member member = memberService.getMemberByName(username);
        if (!comment.getMember().equals(member)) {
            throw new NoMasterException();
        }
    }

    // 대댓글까지 모두 삭제, dirty checking을 사용
    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        deleteChildComments(comment);
        comment.getDeleteFlag().softDelete();
    }

    /**
     * parallelStream의 경우에는 thread pool을 공유하므로 사용에 주의해야 한다.
     * 참고 : https://multifrontgarden.tistory.com/254
     */
    private void deleteChildComments(Comment comment) {
        comment.getChildComments().forEach(childComment -> childComment.getDeleteFlag().softDelete());
    }
}
