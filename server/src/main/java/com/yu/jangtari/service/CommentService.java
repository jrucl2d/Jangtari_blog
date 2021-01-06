package com.yu.jangtari.service;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.MemberRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<CommentDTO.Get> getComments(Long postId) throws CustomException {

        return postRepository.getCommentsOfPost(postId);
    }

    @Transactional
    public void addComment(CommentDTO.Add comment) throws CustomException {
        if(comment.getComment().equals("") || comment.getComment() == null){
            throw new CustomException("입력 정보가 충분하지 않습니다.", "댓글 추가 실패");
        }
        Comment comment1 = new Comment();
        Post post = new Post();
        Optional<Member> member = memberRepository.findByUsername(comment.getCommenter());
        if (!member.isPresent()){
            throw new CustomException("사용자가 존재하지 않습니다.", "댓글 추가 실패");
        }
        post.setId(comment.getPostId());
        comment1.setPost(post);
        comment1.setComment(comment.getComment());
        comment1.setMember(member.get());
        if(comment.getRecommentId() != null){
            Object parentRecomment = commentRepository.getRecommentByRecommentId(comment.getRecommentId());
            String theString = "";
            if(parentRecomment == null){
                theString += comment.getRecommentId();
            } else{
                theString += parentRecomment.toString() + "-" + comment.getRecommentId();
            }
            comment1.setRecomment(theString);
        }
        commentRepository.save(comment1);
    }

    @Transactional
    public void updateComment(CommentDTO.Update comment) throws  CustomException {
        if(comment.getComment().equals("") || comment.getComment() == null){
            throw new CustomException("입력 정보가 충분하지 않습니다.", "댓글 수정 실패");
        }
        Optional<Comment> comment1 = commentRepository.findById(comment.getId());
        if(comment1.isPresent()){
            comment1.get().setComment(comment.getComment());
            commentRepository.save(comment1.get());
        } else {
            throw new CustomException("존재하지 않는 댓글입니다.", "게시글 수정 실패 : id = " + comment.getId());
        }
    }

    @Transactional
    public void deleteComment(Long commentId) throws CustomException{
        try{
            String commentString = "" + commentId;
            commentRepository.deleteUnderCommentId(commentId, commentString);
        } catch (Exception e){
            throw new CustomException("존재하지 않는 댓글입니다.", "댓글 삭제 실패 : id = " + commentId);
        }
    }
}
