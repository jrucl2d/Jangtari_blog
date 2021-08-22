package com.yu.jangtari.api.post.repository.post;

import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.common.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface CustomPostRepository {
    Optional<Post> findJoining(Long postId);
    Page<PostDto.ListGetElement> findPostList(Long categoryId, PageRequest pageRequest);
}
