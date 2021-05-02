package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.FileTaskException;
import com.yu.jangtari.common.exception.GoogleDriveException;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.domain.DTO.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final GoogleDriveUtil googleDriveUtil;


    public Post addPost(PostDTO.Add postDTO) {
        // 1. Category 객체 find
        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                () -> new NoSuchCategoryException()
        );

        // 2. Post 객체 save, Picture 객체 save(영속성 전이)
        Post forSavePost = postDTO.toEntity(category);
        forSavePost = addPicturesToPostIfExist(forSavePost, postDTO.getPictures());
        Post savedPost = postRepository.save(forSavePost);

        // 3. Hashtag 객체 save
        List<Hashtag> hashtags = postDTO.getHashtags();
        hashtagRepository.saveAll(hashtags);

        // 5. Post객체의 영속성 전이를 이용해 PostHashtag 저장
        savedPost.initPostHashtags(hashtags);
        postRepository.save(savedPost);
        System.out.println(forSavePost);
        return savedPost;
    }

    private Post addPicturesToPostIfExist(Post forSavePost, List<MultipartFile> pictureFiles) {
        if (!pictureFiles.isEmpty()) {
            try {
                List<String> pictureURLs = fileToURL(pictureFiles);
                forSavePost.initPictures(pictureURLs);
            } catch(GeneralSecurityException e1) {
                throw new GoogleDriveException();
            } catch (IOException e2) {
                throw new FileTaskException();
            }
        }
        return forSavePost;
    }
    /**
     * parallelStream을 이용, 병렬성을 이용해 사진 저장 속도 상승
     * @param pictureFiles
     * @return picture URL의 List
     * @throws GeneralSecurityException Google Drive API 사용중 발생한 예외
     * @throws IOException googleDriveUtil.getDrive()시에 발생할 수 있는 예외
     */
    private List<String> fileToURL(List<MultipartFile> pictureFiles) throws GeneralSecurityException, IOException {
        return googleDriveUtil.fileToURL(pictureFiles, GDFolder.POST);
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
