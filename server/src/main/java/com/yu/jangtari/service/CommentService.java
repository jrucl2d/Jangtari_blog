package com.yu.jangtari.service;

import com.yu.jangtari.common.exception.NoSuchCommentException;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;
//    private PostRepository postRepository;

//    @Transactional(readOnly = true)
//    public List<CommentDTO.Get> getComments(Long postId) throws CustomException {
//
//        return postRepository.getCommentsOfPost(postId);
//    }
//
//    @Transactional
//    public void addComment(CommentDTO.Add comment) throws CustomException {
//
//        if(comment.getComment().equals("") || comment.getComment() == null){
//            throw new CustomException("입력 정보가 충분하지 않습니다.", "댓글 추가 실패");
//        }
//        Comment comment1 = new Comment();
//        Post post = new Post();
//        Optional<Member> member = memberRepository.findByUsername(comment.getCommenter());
//        if (!member.isPresent()){
//            throw new CustomException("사용자가 존재하지 않습니다.", "댓글 추가 실패");
//        }
//        post.setId(comment.getPostId());
//        comment1.setPost(post);
//        comment1.setComment(comment.getComment());
//        comment1.setMember(member.get());
//        if(comment.getRecommentId() != null){
//            Optional<Comment> parentComment = commentRepository.findById(comment.getRecommentId());
//            if(!parentComment.isPresent()){
//                throw new CustomException("해당 댓글이 존재하지 않습니다.", "댓글 추가 실패");
//            }
//            comment1.setRecomment(parentComment.get());
//            List<Comment> beforeSubcomment = parentComment.get().getSubcomment();
//            beforeSubcomment.add(comment1);
//        } else {
//            comment1.setRecomment(comment1);
//            comment1.setSubcomment(Arrays.asList(comment1));
//        }
//        commentRepository.save(comment1);
//    }
//
//    @Transactional
//    public void updateComment(CommentDTO.Update comment) throws  CustomException {
//        if(comment.getComment().equals("") || comment.getComment() == null){
//            throw new CustomException("입력 정보가 충분하지 않습니다.", "댓글 수정 실패");
//        }
//        Optional<Comment> comment1 = commentRepository.findById(comment.getId());
//        if(comment1.isPresent()){
//            comment1.get().setComment(comment.getComment());
//            commentRepository.save(comment1.get());
//        } else {
//            throw new CustomException("존재하지 않는 댓글입니다.", "게시글 수정 실패 : id = " + comment.getId());
//        }
//    }

    // 대댓글까지 모두 삭제, dirty checking을 사
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchCommentException());
        deleteChildcomments(comment);
        comment.getDeleteFlag().softDelete();
    }

    /**용
     * 코드 내부의 여러 parallelStream들은 thread pool을 공유하므로 사용에 주의해야 한다.
     * 참고 : https://multifrontgarden.tistory.com/254
     */
    private void deleteChildcomments(Comment comment) {
        comment.getSubcomments().parallelStream().forEach(subcomment -> subcomment.getDeleteFlag().softDelete());
    }
}
