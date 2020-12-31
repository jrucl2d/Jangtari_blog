package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository{
}
