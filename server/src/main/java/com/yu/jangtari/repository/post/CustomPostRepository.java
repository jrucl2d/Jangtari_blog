package com.yu.jangtari.repository.post;

import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.Post;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface CustomPostRepository {
    Optional<Post> getOne(Long postId);
    Page<Post> getPostList(Long categoryId, PageRequest pageRequest);
}
