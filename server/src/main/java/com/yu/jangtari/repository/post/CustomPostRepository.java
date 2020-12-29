package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Post;

import java.util.List;

public interface CustomPostRepository {
    public List<PostDTO.GetAll> getPostList(Long categoryId);

    public List<Hashtag> getHashtags(List<String> hashtags);
}
