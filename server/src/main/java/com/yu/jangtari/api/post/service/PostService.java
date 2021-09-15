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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public PostDto.GetOne getPost(Long postId) {
        return PostDto.GetOne.of(getOneJoining(postId));
    }

    private Post getOneJoining(Long postId) {
        return postRepository.findJoining(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_ERROR));
    }

    @Transactional(readOnly = true)
    public Page<PostDto.ListGetElement> getPostList(Long categoryId, PageRequest pageRequest) {
        return postRepository.findPostList(categoryId, pageRequest);
    }

    public PostDto.ListGetElement addPost(PostDto.Add postDto, List<MultipartFile> pictures) {
        try {
            Post post = Post.of(postDto.toUrlDto(googleDriveUtil, pictures), hashtagRepository);
            return PostDto.ListGetElement.of(postRepository.save(post));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
        }
    }

    public PostDto.GetOne updatePost(PostDto.Update postDto, List<MultipartFile> pictures) {
        Post post = getOneJoining(postDto.getPostId());
        post.updatePost(postDto.toUrlDto(googleDriveUtil, pictures), hashtagRepository);
        postRepository.save(post);
        System.out.println("하하하하ㅏ");
        return PostDto.GetOne.of(post);
    }

    public void deletePost(Long postId) {
        Post post = getOneJoining(postId);
        post.softDelete();
    }
}
