package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post.id = :postId and c.deleteFlag.deleteFlag = false")
    List<Comment> findCommentsOfPost(@Param(value = "postId") Long postId);
}
