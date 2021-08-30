package com.yu.jangtari.api.post.service;

import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.repository.hashtag.HashtagRepository;
import com.yu.jangtari.api.post.repository.post.PostRepository;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public Post getOneJoining(Long postId) {
        return postRepository.findJoining(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_ERROR));
    }

    @Transactional(readOnly = true)
    public Post getOne(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_ERROR));
    }

    @Transactional(readOnly = true)
    public Page<PostDto.ListGetElement> getPostList(Long categoryId, PageRequest pageRequest) {
        return postRepository.findPostList(categoryId, pageRequest);
    }

    public Post addPost(PostDto.Add postDto) {
        try {
            return postRepository.save(Post.of(postDto.toUrlDto(googleDriveUtil), hashtagRepository));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
        }
    }

    public Post updatePost(Long postId, PostDto.Update postDto) {
        Post post = getOne(postId);
        return post.updatePost(postDto.toUrlDto(googleDriveUtil), hashtagRepository);
    }

    public void deletePost(Long postId) {
        Post post = getOneJoining(postId);
        post.softDelete();
    }
}
