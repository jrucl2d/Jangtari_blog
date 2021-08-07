package com.yu.jangtari.api.post.repository.post;

import com.yu.jangtari.api.post.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository {
}
