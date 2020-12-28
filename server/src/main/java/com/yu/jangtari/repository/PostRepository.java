package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
