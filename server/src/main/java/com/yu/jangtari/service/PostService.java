package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.common.exception.NoSuchPostException;
import com.yu.jangtari.util.GoogleDriveUtil;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.PictureRepository;
import com.yu.jangtari.repository.PostHashtagRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.domain.DTO.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PictureRepository pictureRepository;
    private final GoogleDriveUtil googleDriveUtil;

    // Comment, PostHashtag, Picture을 join해서 같이 가져옴
    @Transactional(readOnly = true)
    public Post getOne(final Long postId) {
        return postRepository.getOne(postId).orElseThrow(NoSuchPostException::new).getDeleteFiltered();
    }
    public Post findOne(final Long postId) {
        return postRepository.findById(postId).orElseThrow(NoSuchPostException::new);
    }

    @Transactional(readOnly = true)
    public Page<PostDTO.Get> getPostList(Long categoryId, PageRequest pageRequest) {
        return postRepository.getPostList(categoryId, pageRequest);
    }

    public Post addPost(PostDTO.Add postDTO) {
        final Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(NoSuchCategoryException::new);
        final Post post = postRepository.save(postDTO.toEntity(category));
        addPicturesToPostIfExist(post, postDTO.getPictures());
        addHashtagsToPostIfExist(post, postDTO.takeHashtagsEntity());
        return post;
    }
    /**
     * update 과정에서의 hashtag와 picture은 실제로 삭제한다.
     * hashtag는 모두 삭제하고 추가한다.
     * picture는 google drive에 업로드하는 데 걸리는 오버헤드를 생각해 프론트에서 삭제할 picture와 추가할 picture를 따로 받음
     */
    public Post updatePost(Long postId, PostDTO.Update postDTO) {
        final Post post = findOne(postId);
        post.updateTitleContentTemplate(postDTO);
        updatePicturesOfPostIfExist(post, postDTO);
        post.clearPostHashtags();
        addHashtagsToPostIfExist(post, postDTO.getHashtagsEntity());
        return post;
    }
    private void addHashtagsToPostIfExist(final Post post, final List<Hashtag> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) return;
        hashtagRepository.saveAll(hashtags);
        final List<PostHashtag> postHashtags = PostHashtag.hashtagsToPostHashtags(hashtags, post);
        postHashtagRepository.saveAll(postHashtags);
        post.addPostHashtags(postHashtags);
    }
    private void addPicturesToPostIfExist(final Post post, final List<MultipartFile> pictureFiles) {
        if (pictureFiles == null || pictureFiles.isEmpty()) return;
        final List<String> pictureURLs = googleDriveUtil.filesToURLs(pictureFiles, GDFolder.POST);
        final List<Picture> pictures = Picture.stringsToPictures(pictureURLs, post);
        pictureRepository.saveAll(pictures);
        post.addPictures(pictures);
    }
    private void updatePicturesOfPostIfExist(final Post post, final PostDTO.Update postDTO) {
        post.removePicturesFromUpdateDTO(postDTO);
        addPicturesToPostIfExist(post, postDTO.getAddPics());
    }

    // Post에 연관된 Comment, Post-Hashtag, Picture을 softDelete 처리해야 함
    public void deletePost(Long postId) {
        final Post post = postRepository.getOne(postId).orElseThrow(NoSuchPostException::new);
        deleteRelationOfPost(post);
    }
    public void deletePostsOfCategory(Long categoryId) {
        final List<Post> posts = postRepository.getPostListForDelete(categoryId);
        posts.forEach(this::deleteRelationOfPost);
    }
    private void deleteRelationOfPost(Post post) {
        post.getComments().forEach(comment -> comment.getDeleteFlag().softDelete());
        post.getPictures().forEach(picture -> picture.getDeleteFlag().softDelete());
        post.getPostHashtags().forEach(postHashtag -> postHashtag.getDeleteFlag().softDelete());
        post.getDeleteFlag().softDelete();
    }
}
