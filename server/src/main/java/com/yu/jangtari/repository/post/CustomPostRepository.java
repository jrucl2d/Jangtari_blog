package com.yu.jangtari.repository.post;

import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CustomPostRepository {
    Optional<Post> getOne(Long postId);
    Page<PostDTO.GetList> getPostList(Long categoryId, PageRequest pageRequest);
    List<Post> getPostListForDelete(@Param(value = "categoryId") Long categoryId);
}
