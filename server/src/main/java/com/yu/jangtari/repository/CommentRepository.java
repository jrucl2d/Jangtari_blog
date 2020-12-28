package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
