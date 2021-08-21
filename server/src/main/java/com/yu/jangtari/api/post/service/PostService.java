package com.yu.jangtari.api.post.service;

import com.yu.jangtari.api.picture.domain.Picture;
import com.yu.jangtari.api.picture.repository.PictureRepository;
import com.yu.jangtari.api.post.domain.Hashtag;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.domain.PostHashtag;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.repository.hashtag.HashtagRepository;
import com.yu.jangtari.api.post.repository.hashtag.PostHashtagRepository;
import com.yu.jangtari.api.post.repository.post.PostRepository;
import com.yu.jangtari.common.GDFolder;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PictureRepository pictureRepository;
    private final GoogleDriveUtil googleDriveUtil;

    /**
     * Comment, PostHashtag, Picture 함께 join
     */
    @Transactional(readOnly = true)
    public Post getOneJoining(Long postId) {
        return postRepository.findJoining(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_ERROR));
    }

    public Post getOne(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_ERROR));
    }

    @Transactional(readOnly = true)
    public Page<PostDto.Get> getPostList(Long categoryId, PageRequest pageRequest) {
        return postRepository.findPostList(categoryId, pageRequest);
    }

    public Post addPost(PostDto.Add postDto) {
        try {
            return postRepository.save(Post.of(postDto.toUrlDto(googleDriveUtil)));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
        }
    }
    /**
     * update 과정에서의 hashtag와 picture은 실제로 삭제한다.
     * hashtag는 모두 삭제하고 추가한다.
     * picture는 google drive에 업로드하는 데 걸리는 오버헤드를 생각해 프론트에서 삭제할 picture와 추가할 picture를 따로 받음
     */
    public Post updatePost(final Long postId, final PostDto.Update postDTO) {
        final Post post = getOne(postId);
        post.updatePost(postDTO);
        updatePicturesOfPostIfExist(post, postDTO);
        post.clearPostHashtags();
        addHashtagsToPostIfExist(post, postDTO.getHashtagsEntities());
        return post;
    }
    private void addHashtagsToPostIfExist(final Post post, final List<Hashtag> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) return;
        hashtagRepository.saveAll(hashtags);
        final List<PostHashtag> postHashtags = hashtags.stream().map(ht -> PostHashtag.builder().hashtag(ht).post(post).build()).collect(Collectors.toList());
        postHashtagRepository.saveAll(postHashtags);
        post.addPostHashtags(postHashtags);
    }
    private void addPicturesToPostIfExist(final Post post, final List<MultipartFile> pictureFiles) {
        if (pictureFiles == null || pictureFiles.isEmpty()) return;
        final List<String> pictureURLs = googleDriveUtil.filesToURLs(pictureFiles, GDFolder.POST);
        final List<Picture> pictures = pictureURLs.stream().map(url -> Picture.builder().url(url).post(post).build()).collect(Collectors.toList());
        pictureRepository.saveAll(pictures);
        post.addPictures(pictures);
    }
    private void updatePicturesOfPostIfExist(final Post post, final PostDto.Update postDTO) {
        post.removePictures(postDTO);
        addPicturesToPostIfExist(post, postDTO.getAddPics());
    }

    public void deletePost(Long postId) {
        Post post = getOneJoining(postId);
        post.softDelete();
    }
}
