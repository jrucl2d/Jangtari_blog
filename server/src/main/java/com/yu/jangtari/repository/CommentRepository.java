package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c.recomment FROM Comment c WHERE c.id=:recommentId")
    public Object getRecommentByRecommentId(@Param(value = "recommentId") Long recommentId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id= :commentId or c.recomment LIKE %:commentString%")
    public void deleteUnderCommentId(@Param(value="commentId")Long commentId, @Param(value = "commentString") String commentString);
}
