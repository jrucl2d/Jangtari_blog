package com.yu.jangtari.service;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void addComment(CommentDTO.Add comment) throws CustomException {
        if(comment.getComment().equals("") || comment.getComment() == null){
            throw new CustomException("입력 정보가 충분하지 않습니다.", "댓글 추가 실패");
        }
        Comment comment1 = new Comment();
        Post post = new Post();
        // Principal에서 가져오기 principal.getName(); 이런식
        Member member = memberRepository.findByUsername("jangtari");
        post.setId(comment.getPostId());
        comment1.setPost(post);
        comment1.setComment(comment.getComment());
        comment1.setMember(member);
        if(comment.getRecomment() != null){
            Comment comment2 = new Comment();
            comment2.setId(comment.getRecomment());
            comment1.setRecomment(comment2);
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
}
