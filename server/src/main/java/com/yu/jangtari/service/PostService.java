package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchPostException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.domain.DTO.PostDTO;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public Post getOne(final Long postId) {
        return findOne(postId);
    }

    public Post findOne(final Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchPostException());
        return post;
    }
    private void addPicturesToPostIfExist(final Post forSavePost, final List<MultipartFile> pictureFiles) {
        final List<String> pictureURLs = googleDriveUtil.filesToURLs(pictureFiles, GDFolder.POST);
        forSavePost.initPictures(pictureURLs);
    }

//    @Transactional(readOnly = true)
//    public PageMakerVO<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO, String type, String keyword) throws CustomException {
//        return postRepository.getPostList(categoryId, pageVO, type, keyword);
//    }
//
//    @Transactional(readOnly = true)
//    public PostDTO.GetOne getPost(Long postId) {
//        return postRepository.getPost(postId);
//    }
//    @Transactional
//    public void updatePost(PostDTO.Update thePost, List<MultipartFile> postImages) throws CustomException, GeneralSecurityException, IOException {
//        Optional<Post> postOp = postRepository.findById(thePost.getId());
//        if(postOp.isPresent()){
//            Post post = postOp.get();
//
//            post.setTitle(thePost.getTitle());
//            post.setTemplate(thePost.getTemplate());
//            post.setPost(thePost.getPost());
//
//            // 해시태그 설정
//            if(thePost.getHashtags() != null){
//                postRepository.deleteBeforeHashtags(thePost.getId());
//                List<String> hashtagStrings = new ArrayList<>();
//                thePost.getHashtags().forEach(ht -> {
//                    hashtagStrings.add(ht.getHashtag());
//                });
//                List<Hashtag> hashtags = postRepository.getHashtags(hashtagStrings);
//                hashtags.forEach(ht -> {
//                    hashtagStrings.remove(ht.getHashtag()); // 이미 존재하는 해시태그는 목록에서 삭제
//                    List<Post> beforePosts = ht.getPosts();
//                    beforePosts.add(post);
//                    ht.setPosts(beforePosts);
//                });
//                hashtagStrings.forEach(ht -> {
//                    Hashtag hashtag = new Hashtag();
//                    hashtag.setHashtag(ht);
//                    hashtag.setPosts(Arrays.asList(post));
//                    hashtags.add(hashtag);
//                });
//                post.setHashtags(hashtags);
//            }
//
//            // 사진 추가
//            if(postImages != null){
//                // 삭제할 사진 삭제
//                if(thePost.getDelPics().size() > 0){
//                    pictureRepository.deleteByIds(thePost.getDelPics());
//                }
//                List<Picture> pictures = new ArrayList<>();
//                Drive drive = googleDriveUtil.getDrive();
//                for(MultipartFile postImage : postImages) {
//                    File file = new File();
//                    file.setName(googleDriveUtil.getPictureName(postImage.getName()));
//                    file.setParents(Collections.singletonList(googleDriveUtil.POST_FOLDER));
//                    java.io.File tmpFile = googleDriveUtil.convert(postImage);
//                    FileContent content = new FileContent("image/jpeg", tmpFile);
//                    File uploadedFile = drive.files().create(file, content).setFields("id").execute();
//                    tmpFile.delete();
//                    String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
//                    Picture picture = new Picture();
//                    picture.setPost(post);
//                    picture.setPicture(fileRef);
//                    pictureRepository.save(picture);
//                }
//                post.setPictures(pictures);
//            }
//            postRepository.save(post);
//        } else {
//            throw new CustomException("존재하지 않는 게시글입니다.", "게시글 수정 실패 : id = " + thePost.getId());
//        }
//    }
//
//    public void deletePost(Long postId) throws CustomException{
//        try{
//            postRepository.deleteById(postId);
//        } catch (Exception e){
//            throw new CustomException("존재하지 않는 게시글입니다.", "게시글 삭제 실패 : id = " + postId);
//        }
//    }
}
