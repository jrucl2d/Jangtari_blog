package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.DTO.PostDTO;

import java.util.List;

public interface CustomPostRepository {
    public List<PostDTO.GetAll> getPostList(Long categoryId);
}
