package com.yu.jangtari.service;

import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Picture;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostDTO.GetAll> getPostList(Long categoryId) throws CustomException {
        List<PostDTO.GetAll> result = postRepository.getPostList(categoryId);
        return result;
    }

    @Transactional
    public void addPost(PostDTO.Add thePost) throws CustomException {
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
        if(thePost.getHashtags().size() > 0){
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
        if(thePost.getPictures().size() > 0){
            List<Picture> pictures = new ArrayList<>();
            thePost.getPictures().forEach(p -> {
                Picture picture = new Picture();
                picture.setPicture(p.getPicture());
                picture.setPost(post);
                pictures.add(picture);
            });
            post.setPictures(pictures);
        }
        postRepository.save(post);
    }
}
