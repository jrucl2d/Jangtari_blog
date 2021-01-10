package com.yu.jangtari.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.vo.PageMakerVO;
import com.yu.jangtari.vo.PageVO;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Picture;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.PictureRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private GoogleDriveUtil googleDriveUtil;

    @Transactional
    public void deleteTrashHashtags(){
        postRepository.deleteTrashHashtags();
    }

    @Transactional(readOnly = true)
    public PageMakerVO<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO, String type, String keyword) throws CustomException {
        return postRepository.getPostList(categoryId, pageVO, type, keyword);
    }

    @Transactional(readOnly = true)
    public PostDTO.GetOne getPost(Long postId) {
        return postRepository.getPost(postId);
    }

    @Transactional
    public void addPost(PostDTO.Add thePost, List<MultipartFile> postImages) throws CustomException, GeneralSecurityException, IOException {
        if(thePost.getTitle().equals("") || thePost.getTitle() == null || thePost.getPost().equals("") || thePost.getPost() == null){
            throw new CustomException("입력 정보가 충분하지 않습니다.", "게시글 추가 실패");
        }
        Post post = new Post();
        Category category = new Category();
        category.setId(thePost.getCategoryId());
        post.setCategory(category);
        post.setTitle(thePost.getTitle());
        post.setPost(thePost.getPost());
        post.setTemplate(thePost.getTemplate());

        // 해시태그 추가
        if(thePost.getHashtags() != null){
            List<String> hashtagStrings = new ArrayList<>();
            thePost.getHashtags().forEach(ht -> {
                hashtagStrings.add(ht.getHashtag());
            });
            List<Hashtag> hashtags = postRepository.getHashtags(hashtagStrings);
            hashtags.forEach(ht -> {
                hashtagStrings.remove(ht.getHashtag()); // 이미 존재하는 해시태그는 목록에서 삭제
                List<Post> beforePosts = ht.getPosts();
                beforePosts.add(post);
                ht.setPosts(beforePosts);
            });
            hashtagStrings.forEach(ht -> {
                Hashtag hashtag = new Hashtag();
                hashtag.setHashtag(ht);
                hashtag.setPosts(Arrays.asList(post));
                hashtags.add(hashtag);
            });
            post.setHashtags(hashtags);
        }

        // 사진 추가
        if(postImages != null){
            List<Picture> pictures = new ArrayList<>();
            Drive drive = googleDriveUtil.getDrive();
            for(MultipartFile postImage : postImages) {
                File file = new File();
                file.setName(googleDriveUtil.getPictureName(postImage.getName()));
                file.setParents(Collections.singletonList(googleDriveUtil.POST_FOLDER));
                java.io.File tmpFile = googleDriveUtil.convert(postImage);
                FileContent content = new FileContent("image/jpeg", tmpFile);
                File uploadedFile = drive.files().create(file, content).setFields("id").execute();
                tmpFile.delete();
                String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
                Picture picture = new Picture();
                picture.setPost(post);
                picture.setPicture(fileRef);
                pictureRepository.save(picture);
            }
            post.setPictures(pictures);
        }
        postRepository.save(post);
    }

    @Transactional
    public void updatePost(PostDTO.Update thePost, List<MultipartFile> postImages) throws CustomException, GeneralSecurityException, IOException {
        Optional<Post> postOp = postRepository.findById(thePost.getId());
        if(postOp.isPresent()){
            Post post = postOp.get();

            post.setTitle(thePost.getTitle());
            post.setTemplate(thePost.getTemplate());
            post.setPost(thePost.getPost());

            // 해시태그 설정
            if(thePost.getHashtags() != null){
                postRepository.deleteBeforeHashtags(thePost.getId());
                List<String> hashtagStrings = new ArrayList<>();
                thePost.getHashtags().forEach(ht -> {
                    hashtagStrings.add(ht.getHashtag());
                });
                List<Hashtag> hashtags = postRepository.getHashtags(hashtagStrings);
                hashtags.forEach(ht -> {
                    hashtagStrings.remove(ht.getHashtag()); // 이미 존재하는 해시태그는 목록에서 삭제
                    List<Post> beforePosts = ht.getPosts();
                    beforePosts.add(post);
                    ht.setPosts(beforePosts);
                });
                hashtagStrings.forEach(ht -> {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setHashtag(ht);
                    hashtag.setPosts(Arrays.asList(post));
                    hashtags.add(hashtag);
                });
                post.setHashtags(hashtags);
            }

            // 사진 추가
            if(postImages != null){
                List<Picture> pictures = new ArrayList<>();
                pictureRepository.deleteByPostId(thePost.getId());
                Drive drive = googleDriveUtil.getDrive();
                for(MultipartFile postImage : postImages) {
                    File file = new File();
                    file.setName(googleDriveUtil.getPictureName(postImage.getName()));
                    file.setParents(Collections.singletonList(googleDriveUtil.POST_FOLDER));
                    java.io.File tmpFile = googleDriveUtil.convert(postImage);
                    FileContent content = new FileContent("image/jpeg", tmpFile);
                    File uploadedFile = drive.files().create(file, content).setFields("id").execute();
                    tmpFile.delete();
                    String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
                    Picture picture = new Picture();
                    picture.setPost(post);
                    picture.setPicture(fileRef);
                    pictureRepository.save(picture);
                }
                post.setPictures(pictures);
            }
            postRepository.save(post);
        } else {
            throw new CustomException("존재하지 않는 게시글입니다.", "게시글 수정 실패 : id = " + thePost.getId());
        }
    }

    public void deletePost(Long postId) throws CustomException{
        try{
            postRepository.deleteById(postId);
        } catch (Exception e){
            throw new CustomException("존재하지 않는 게시글입니다.", "게시글 삭제 실패 : id = " + postId);
        }
    }
}
