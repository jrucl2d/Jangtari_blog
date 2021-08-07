package com.yu.jangtari.api.comment.repository;

import com.yu.jangtari.api.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.member m where c.post.id = :postId and c.deleteFlag.deleteFlag = false order by c.createdDate asc")
    List<Comment> findCommentsOfPost(@Param(value = "postId") Long postId);
}
