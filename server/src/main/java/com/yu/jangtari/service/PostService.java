package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.common.exception.NoSuchPostException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.repository.HashtagRepository;
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

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final GoogleDriveUtil googleDriveUtil;
    private final CategoryService categoryService;


    // Comment, PostHashtag, Picture을 join해서 같이 가져옴
    @Transactional(readOnly = true)
    public Post getOne(final Long postId) {
        return postRepository.getOne(postId).orElseThrow(() -> new NoSuchPostException());
    }
    public Post findOne(final Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchPostException());
        return post;
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostList(Long categoryId, PageRequest pageRequest) {
        return postRepository.getPostList(categoryId, pageRequest);
    }

    public Post addPost(PostDTO.Add postDTO) {
        // 1. Category 객체 get
        final Category category = categoryService.findOne(postDTO.getCategoryId());

        // 2. Post 객체 save, Picture 객체 save(영속성 전이)
        final Post forSavePost = postDTO.toEntity(category);
        addPicturesToPostIfExist(forSavePost, postDTO.getPictures());
        final Post savedPost = postRepository.save(forSavePost);

        // 3. Hashtag 객체 save
        final List<Hashtag> hashtags = hashtagRepository.saveAll(postDTO.getHashtags());

        // 5. Post객체의 영속성 전이를 이용해 PostHashtag 저장
        savedPost.initPostHashtags(hashtags);
        postRepository.save(savedPost);
        return savedPost;
    }

    private void addPicturesToPostIfExist(final Post forSavePost, final List<MultipartFile> pictureFiles) {
        final List<String> pictureURLs = googleDriveUtil.filesToURLs(pictureFiles, GDFolder.POST);
        forSavePost.addPictures(pictureURLs);
    }
    private void updatePicturesOfPostIfExist(final Post post, final PostDTO.Update postDTO) {
        post.removePicturesFromUpdateDTO(postDTO);
        addPicturesToPostIfExist(post, postDTO.getAddPics());
    }

    /**
     * update 과정에서의 hashtag와 picture은 실제로 삭제한다.
     * hashtag는 모두 삭제하고 추가한다.
     * picture는 google drive에 업로드하는 데 걸리는 오버헤드를 생각해 프론트에서 삭제할 picture와 추가할 picture를 따로 받음
     */
    public Post updatePost(PostDTO.Update postDTO) {
        // 1. Post 객체 get
        final Post post = findOne(postDTO.getPostId());
        // 2. hashtag, picture 제외한 내용 update
        post.updateTitleContentTemplate(postDTO);
        // 3. 기존의 hashtag들 실제로 삭제, 새로운 hashtag 추가
        post.clearPostHashtags();
        final List<Hashtag> hashtags = hashtagRepository.saveAll(postDTO.getHashtags());
        post.initPostHashtags(hashtags);
        // 4. 삭제할 picture들을 실제로 삭제, 새로운 picture 추가
        updatePicturesOfPostIfExist(post, postDTO);
        return post;
    }

    /**
     * Post에 연관된 Comment, Post-Hashtag, Picture을 softDelete 처리해야 함
     */
    public void deletePost(Long postId) {
        Post post = getOne(postId);
        post.getComments().forEach(comment -> comment.getDeleteFlag().softDelete());
        post.getPictures().forEach(picture -> picture.getDeleteFlag().softDelete());
        post.getPostHashtags().forEach(postHashtag -> postHashtag.getDeleteFlag().softDelete());
        post.getDeleteFlag().softDelete();
    }
    public void deletePostOfCategory(Long categoryId) {
        
    }
}
