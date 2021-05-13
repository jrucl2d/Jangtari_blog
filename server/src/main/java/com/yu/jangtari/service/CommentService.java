package com.yu.jangtari.service;

import com.yu.jangtari.common.exception.NoMasterException;
import com.yu.jangtari.common.exception.NoSuchCommentException;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
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
    public List<Comment> getCommentsOfPost(final Long postId) {
        return commentRepository.findCommentsOfPost(postId);
    }

    @Transactional(readOnly = true)
    public Comment getComment(final Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NoSuchCommentException());
    }

    public Comment addComment(final CommentDTO.Add commentDTO) {
        final Comment comment = commentDTO.toEntity();
        final Post post = postService.findOne(commentDTO.getPostId());
        final Member member = memberService.getMemberByName(commentDTO.getCommenter());
        comment.initPostAndMember(post, member);
        addParentIfExists(comment, commentDTO.getParentCommentId());
        return commentRepository.save(comment);
    }
    private void addParentIfExists(final Comment comment, final Long parentId) {
        if (parentId == null) return;
        final Comment parentComment = getComment(parentId);
        parentComment.addChildComment(comment);
    }

    public Comment updateComment(final CommentDTO.Update commentDTO) {
        Comment comment = getComment(commentDTO.getId());
        verifyCommenter(commentDTO.getCommenter(), comment);
        comment.updateComemnt(commentDTO);
        return comment;
    }

    /**
     * Member 엔티티에 equals, hashCode가 id를 기반으로 구현되어 있다.
     */
    private void verifyCommenter(final String username, final Comment comment) {
        Member member = memberService.getMemberByName(username);
        if (!comment.getMember().equals(member)) {
            throw new NoMasterException();
        }
    }

    // 대댓글까지 모두 삭제, dirty checking을 사용
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchCommentException());
        deleteChildcomments(comment);
        comment.getDeleteFlag().softDelete();
    }

    /**
     * 코드 내부의 여러 parallelStream들은 thread pool을 공유하므로 사용에 주의해야 한다.
     * 참고 : https://multifrontgarden.tistory.com/254
     */
    private void deleteChildcomments(Comment comment) {
        comment.getChildComments().parallelStream().forEach(subcomment -> subcomment.getDeleteFlag().softDelete());
    }
}
