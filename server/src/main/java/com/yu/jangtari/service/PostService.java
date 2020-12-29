package com.yu.jangtari.service;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public List<PostDTO.GetAll> getPostList(Long categoryId) throws CustomException {
        List<PostDTO.GetAll> result = postRepository.getPostList(categoryId);
        return result;
    }
}
