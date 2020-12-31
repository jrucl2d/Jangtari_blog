package com.yu.jangtari.repository.post;

import com.yu.jangtari.vo.PageVO;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomPostRepository {
    public Page<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO);

    public PostDTO.GetOne getPost(Long postId);

    public List<Hashtag> getHashtags(List<String> hashtags);
}
